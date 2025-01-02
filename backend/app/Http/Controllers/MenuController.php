<?php

namespace App\Http\Controllers;

use App\Models\Menu;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class MenuController extends Controller
{
    use HttpResponses;

    public function index(){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $menus = Menu::with('items')->get()->map(function ($menu) use ($user) {
                $menu->items = $menu->items->map(function ($item) use ($user) {
                    $item->isFavorite = (bool)$user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                    return $item;
                });
                return $menu;
            });
            return $this->success(data: $menus, message: "");
        }
        return $this->error('', 'No user', 401);
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
            return $this->success(data: [$menu], message: "Item added to menu", textStatus: "added");
        }
        return $this->error('', 'No user', 401);
    }

    public function update(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $menu = Menu::find($id);
            $data = $request->validate([
                'name' => 'required|unique:menus',
                'description' => 'required',
                'availability' => 'nullable'
            ]);
            $data['availability'] = $data['availability'] ?? 1;
            $menu->update($data);
            return $this->success(data: [$menu], message: "Item was updated", textStatus: "updated");
        }
        return $this->error('', 'No user', 401);
    }

    public function destroy($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $menu = Menu::find($id);
            $menu->delete();
            return $this->success(data: [$menu], message: "Item was deleted", textStatus: "deleted");
        }
        return $this->error('', 'No user', 401);
    }

}
