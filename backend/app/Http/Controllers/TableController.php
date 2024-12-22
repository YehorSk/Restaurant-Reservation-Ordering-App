<?php

namespace App\Http\Controllers;

use App\Models\Table;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class TableController extends Controller
{

    use HttpResponses;

    public function getTables(){
        $user = auth('sanctum')->user();
        if($user){
            $items = Table::get();
            return $this->success(data: $items, message: "");
        }
        return $this->error('', 'No user', 401);
    }

}
