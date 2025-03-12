<?php

namespace App\Http\Controllers;

use App\Models\Menu;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;

class MenuController extends Controller
{
    use HttpResponses;

    public function index(Request $request){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            if(in_array($user->role, ['waiter', 'chef', 'user'])){
                $menus = Menu::with('items')->get()->map(function ($menu) use ($user) {
                    $menu->items = $menu->items->map(function ($item) use ($user) {
                        $item->isFavorite = (bool)$user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                        return $item;
                    });
                    return $menu;
                });
            }else{
                $search = $request->input('search');
                $menus = Menu::query()
                    ->when($search, function ($query, $search) {
                        return $query->where(function ($query) use ($search) {
                            $query->where('name', 'LIKE', "%{$search}%")
                                ->orWhere('description', 'LIKE', "%{$search}%");
                        });
                    })
                    ->paginate(10);
            }
            return $this->success(data: $menus, message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function store(Request $request){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $data = $request->validate([
                'name' => 'required|unique:menus',
                'description' => 'required',
                'availability' => 'nullable'
            ]);
            $data['availability'] = $data['availability'] ?? 1;
            $menu = new Menu($data);
            $menu->save();
            if(!$menu->exists){
                return $this->error('', 'Item was not added', 409);
            }
            return $this->success(data: [$menu], message: __("messages.item_added_to_menu"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function update(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $menu = Menu::find($id);
            $data = $request->validate([
                'name' => ['required', Rule::unique('menus')->ignore($menu->id)],
                'description' => 'required',
                'availability' => 'nullable'
            ]);
            $data['availability'] = $data['availability'] ?? 1;
            $menu->update($data);
            return $this->success(data: [$menu], message: __("messages.item_was_updated"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function destroy($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $menu = Menu::find($id);
            $menu->delete();
            return $this->success(data: [$menu], message: __("messages.item_was_deleted"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

}
