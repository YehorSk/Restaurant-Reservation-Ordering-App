<?php

namespace App\Http\Controllers;

use App\Models\Table;
use App\Models\TimeSlot;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class TimeSlotController extends Controller
{

    use HttpResponses;

    public function index()
    {
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $items = TimeSlot::all();
            return $this->success(data: $items, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function store(Request $request){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $data = $request->validate([
                "start_time" => "required",
                "end_time" => "required",
            ]);
            $item = new TimeSlot($data);
            $item->save();
            return $this->success(data: $item, message: "Item was created");
        }
        return $this->error('', 'No user', 401);
    }

    public function update(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = TimeSlot::find($id);
            $data = $request->validate([
                "start_time" => "required",
                "end_time" => "required",
            ]);
            $item->update($data);
            return $this->success(data: $item, message: "Item was updated", textStatus: "updated");
        }
        return $this->error('', 'No user', 401);
    }

    public function destroy($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = TimeSlot::find($id);
            $item->delete();
            return $this->success(data: [], message: "Item was deleted", textStatus: "deleted");
        }
        return $this->error('', 'No user', 401);
    }
}