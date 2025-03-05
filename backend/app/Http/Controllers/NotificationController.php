<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\FCMNotificationTrait;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class NotificationController extends Controller
{

    use HttpResponses;
    use FCMNotificationTrait;

    public function sendToAll(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role == "admin"){
            $title = $request->input('title');
            $body = $request->input('body');
            $this->sendFCMNotificationToEveryone( $title, $body);
            return $this->success([],"Success");
        }
    }

}
