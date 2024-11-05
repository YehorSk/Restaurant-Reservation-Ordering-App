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
            return response()->json($menus);
        }
    }

    public function store(Request $request){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $data = $request->validate([
                'name' => 'required|unique:menus',
                'description' => 'required',
                'availability' => 'nullable'
            ]);
            $menu = new Menu($data);
            $menu->save();
            if(!$menu->exists){
                return $this->error('', 'Item was not added', 409);
            }
            return $this->success(data: $menu, message: "Item added to menu", textStatus: "added");
        }
    }

}
