<?php

namespace App\Http\Controllers;

use App\Models\MenuItem;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class MenuItemController extends Controller
{
    use HttpResponses;

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
        return $this->error('', 'No user', 401);
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
        return $this->error('', 'No user', 401);
    }

    public function store(Request $request, $menu_id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $validatedData = $request->validate([
                'name' => 'required|string|max:255',
                'short_description' => 'nullable|string',
                'long_description' => 'nullable|string',
                'recipe' => 'nullable|string',
                'picture' => 'nullable|string',
                'price' => 'required|numeric|min:0',
            ]);

            $menuItem = MenuItem::create(array_merge($validatedData, ['menu_id' => $menu_id]));

            return $this->success(data: $menuItem, message: "Menu item added successfully.");
        }

        return $this->error('', 'No user', 401);
    }

    public function update(Request $request, $menu_id, $id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $menuItem = MenuItem::where('menu_id', $menu_id)->where('id', $id)->first();

            if (!$menuItem) {
                return $this->error('', 'Menu item not found', 404);
            }

            // Validate input
            $validatedData = $request->validate([
                'name' => 'required|string|max:255',
                'short_description' => 'nullable|string',
                'long_description' => 'nullable|string',
                'recipe' => 'nullable|string',
                'picture' => 'nullable|string',
                'price' => 'required|numeric|min:0',
            ]);

            $menuItem->update($validatedData);

            return $this->success(data: $menuItem, message: "Menu item updated successfully.");
        }

        return $this->error('', 'No user', 401);
    }

    public function destroy($menu_id, $id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $menuItem = MenuItem::where('menu_id', $menu_id)->where('id', $id)->first();

            if (!$menuItem) {
                return $this->error('', 'Menu item not found', 404);
            }

            $menuItem->delete();

            return $this->success(message: "Menu item deleted successfully.");
        }

        return $this->error('', 'No user', 401);
    }



}
