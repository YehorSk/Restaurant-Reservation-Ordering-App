<?php

namespace App\Http\Controllers;

use App\Models\MenuItem;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class MenuItemController extends Controller
{
    use HttpResponses;

    public function index($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $menuItems = MenuItem::where('menu_id', $id)->get();
            return $this->success(data: $menuItems, message: "");
        }
        return $this->error('', 'No user', 401);
    }
}
