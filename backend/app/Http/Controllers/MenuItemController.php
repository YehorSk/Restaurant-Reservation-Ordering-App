<?php

namespace App\Http\Controllers;

use App\Models\MenuItem;
use App\Models\User;
use App\Traits\FCMNotificationTrait;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\File;
use Illuminate\Validation\Rule;

class MenuItemController extends Controller
{
    use HttpResponses;
    use FCMNotificationTrait;


    public function getStats(){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $data_fav = MenuItem::withCount('favoritedByUsers')
                ->orderBy('favorited_by_users_count', 'desc')
                ->take(10)
                ->get();
            $data_ord = MenuItem::withCount('orders')
                ->orderBy('orders_count', 'desc')
                ->take(10)
                ->get();
            return $this->success(data: [[
                'favorites' => $data_fav,
                'ordered' => $data_ord
            ]], message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function index(Request $request, $id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            if(in_array($user->role, ['waiter', 'chef', 'user'])){
                $menuItems = MenuItem::where('menu_id', $id)->get();
            }else{
                $search = $request->input('search');
                $menuItems = MenuItem::where('menu_id', $id)
                    ->when($search, function ($query, $search) {
                        return $query->where(function ($query) use ($search) {
                            $query->where('name', 'LIKE', "%{$search}%")
                                ->orWhere('long_description', 'LIKE', "%{$search}%")
                                ->orWhere('short_description', 'LIKE', "%{$search}%")
                                ->orWhere('recipe', 'LIKE', "%{$search}%");
                        });
                    })
                    ->paginate(10);
            }
            return $this->success(data: $menuItems, message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function uploadImage(Request $request)
    {
        $request->validate([
            'picture' => 'required|image|mimes:jpeg,png,jpg,gif|max:2048',
            'name' => 'required|nullable|string|max:255',
        ]);

        $imageName = time().'.'. $request->name .'.'.$request->picture->extension();
        $request->picture->storeAs('public/images/menu_items', $imageName);

        return response()->json(['image_path' => 'images/menu_items/'.$imageName]);
    }

    public function store(Request $request, $menu_id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $validatedData = $request->validate([
                'name' => 'required|string|max:255',
                'short_description' => 'string',
                'long_description' => 'string',
                'recipe' => 'string',
                'picture' => 'required|image|mimes:jpeg,png,jpg,gif|max:2048',
                'price' => 'required|numeric|min:0',
            ]);

            $imageName = time().'.'.$request->name . '.' . $request->picture->extension();

            $path = $request->file('picture')->storeAs('public/images/menu_items', $imageName);

            $relativePath = str_replace('public/', '', $path);

            $menuItem = MenuItem::create(array_merge($validatedData, ['menu_id' => $menu_id]));
            $menuItem->picture = $relativePath;
            $menuItem->save();

            return $this->success(data: $menuItem, message: __("messages.menu_item_added_successfully"));
        }

        return $this->error('', __('messages.no_user'), 401);
    }

    public function update(Request $request, $menu_id, $id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $menuItem = MenuItem::where('menu_id', $menu_id)->where('id', $id)->first();

            if (!$menuItem) {
                return $this->error('', 'Menu item not found', 404);
            }

            if ($request->has('picture')) {
                $filePath = storage_path('app/public/' . $menuItem->picture);
                File::delete($filePath);
            }
            $validatedData = $request->validate([
                'name' => ['required', Rule::unique('menu_items')->ignore($menuItem->id)],
                'short_description' => 'string',
                'long_description' => 'string',
                'recipe' => 'string',
                'picture' => 'nullable',
                'price' => 'required|numeric|min:0',
                'availability' => 'required|in:0,1',
            ]);

            $oldPrice = $menuItem->price;
            $oldAvailable = $menuItem->availability;

            $menuItem->update($validatedData);

            if ($oldPrice != $menuItem->price) {
                foreach ($menuItem->users as $user) {
                    $quantity = $user->pivot->quantity;
                    $newPrice = $quantity * $menuItem->price;
                    $user->menuItems()->updateExistingPivot($menuItem->id, [
                        'price' => $newPrice,
                        'quantity' => $quantity
                    ]);
                    $token = $user->devices->pluck('device_token')->first();
                    unset($menuItem->users);
                    $menuItem->isFavorite = (bool)$user->favoriteItems()->where('menu_item_id', $menuItem->id)->exists();
                    $this->sendFCMNotification(
                        $token,
                        "PLATEA",
                        __("messages.cart_item_price_updated", ['itemName' => $menuItem->name, 'newPrice' => $newPrice], $user->language),
                        [
                            'id' => $menuItem->id,
                            'new_price' => $menuItem->price,
                            'new_total_price' => $newPrice,
                        ],
                        'cart_update'
                    );
                }
                $users = User::all();
                foreach ($users as $user) {
                    $token = $user->devices->pluck('device_token')->first();
                    if ($token) {
                        unset($menuItem->users);
                        $this->sendFCMNotification(
                            $token,
                            "",
                            "",
                            [
                                'id' => $menuItem->id,
                                'new_price' => $menuItem->price,
                                'new_total_price' => "0",
                            ],
                            'menu_update'
                        );
                    }
                }
            }
            if($oldAvailable != $menuItem->availability){
                if($menuItem->availability == 0){
                    foreach ($menuItem->users as $user) {
                        $token = $user->devices->pluck('device_token')->first();
                        $this->sendFCMNotification(
                            $token,
                            "PLATEA",
                            __("messages.cart_item_not_available", ['itemName' => $menuItem->name], $user->language),
                            [
                                'id' => $menuItem->id,
                            ],
                            'cart_not_available'
                        );
                    }
                    $menuItem->users()->detach();
                }
                $users = User::all();
                foreach ($users as $user) {
                    $token = $user->devices->pluck('device_token')->first();
                    if ($token) {
                        unset($menuItem->users);
                        $this->sendFCMNotification(
                            $token,
                            "",
                            "",
                            [
                                'id' => $menuItem->id,
                                'availability' => $menuItem->availability,
                            ],
                            'menu_item_availability'
                        );
                    }
                }
            }

            return $this->success(data: $menuItem, message: __("messages.menu_item_updated_successfully"));
        }

        return $this->error('', __('messages.no_user'), 401);
    }

    public function destroy($menu_id, $id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $menuItem = MenuItem::where('menu_id', $menu_id)->where('id', $id)->first();

            if (!$menuItem) {
                return $this->error('', 'Menu item not found', 404);
            }

            $filePath = storage_path('app/public/' . $menuItem->picture);
            File::delete($filePath);

            $menuItem->delete();

            return $this->success(message: __("messages.menu_item_deleted_successfully"));
        }

        return $this->error('', __('messages.no_user'), 401);
    }

}
