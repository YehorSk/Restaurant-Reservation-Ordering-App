<?php

namespace App\Http\Controllers;

use App\Models\RestaurantInfo;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class RestaurantInfoController extends Controller
{

    use HttpResponses;

    public function getRestaurantInfo(){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $data = RestaurantInfo::first();
            return $this->success([$data]);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function updateRestaurantInfo(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $data = $request->validate([
                "name" => "required",
                "description" => "required",
                "address" => "required",
                "phone" => "required",
                "email" => "required",
                "website" => "required",
                "opening_hours" => "required",
            ]);
            $item = RestaurantInfo::first();
            $item->update($data);
            return $this->success([$item], __("messages.item_was_updated"));
        }
    }
}
