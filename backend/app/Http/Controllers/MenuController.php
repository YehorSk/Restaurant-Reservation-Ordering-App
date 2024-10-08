<?php

namespace App\Http\Controllers;

use App\Models\Menu;
use Illuminate\Http\Request;

class MenuController extends Controller
{
    public function index(){
        $menus = Menu::with('items')->get();
        return response()->json($menus);
    }

}
