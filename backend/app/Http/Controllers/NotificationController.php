<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class NotificationController extends Controller
{

    use HttpResponses;

    public function index(){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $data = $user->notifications()->get();
            return $this->success($data);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function read($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $notification = $user->notifications()->find($id);
            $notification->update(['read' => true]);
            return $this->success([$notification]);
        }
    }

}
