<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class FavoriteController extends Controller
{
    use HttpResponses;

    public function getUserFavoriteItems(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->favoriteItems()->get();
            return $this->success(data: $items, message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function addUserFavoriteItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $user->favoriteItems()->attach($request->input('menu_item_id'));
            return $this->success(data: [""], message: __("messages.item_added_to_favorites"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function deleteUserFavoriteItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $user->favoriteItems()->detach($request->input('menu_item_id'));
            return $this->success(data: [""], message: __("messages.item_removed_from_favorites"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }


}
