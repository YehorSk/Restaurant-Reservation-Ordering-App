<?php

namespace App\Http\Controllers;

use App\Models\Menu;
use App\Models\User;
use Illuminate\Http\Request;

class MenuController extends Controller
{
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

}
