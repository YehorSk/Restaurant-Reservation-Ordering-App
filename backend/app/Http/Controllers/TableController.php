<?php

namespace App\Http\Controllers;

use App\Models\Table;
use App\Models\TimeSlot;
use App\Models\User;
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

    public function index()
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $items = Table::all();
            return $this->success(data: $items, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function store(Request $request)
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $data = $request->validate([
                "number" => "required",
                "capacity" => "required",
            ]);
            $item = new Table($data);
            $item->save();
            return $this->success(data: $item, message: __("messages.item_was_added"));
        }
        return $this->error('', 'No user', 401);
    }

    public function update(Request $request, $id)
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = Table::find($id);
            $data = $request->validate([
                "number" => "required",
                "capacity" => "required",
            ]);
            $item->update($data);
            return $this->success(data: $item, message: __("messages.item_was_updated"));
        }
        return $this->error('', 'No user', 401);
    }

    public function destroy($id)
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = Table::find($id);
            $item->delete();
            return $this->success(data: [], message: __("messages.item_was_deleted"));
        }
        return $this->error('', 'No user', 401);
    }


}
