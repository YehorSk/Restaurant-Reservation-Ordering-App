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
        return $this->error('', __('messages.no_user'), 401);
    }

    public function store(Request $request){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $data = $request->validate([
                "start_time" => ["required", "date_format:H:i"],
                "end_time" => ["required", "date_format:H:i", "after:start_time"],
            ]);
            $checkItems = TimeSlot::where('start_time', $data['start_time'])
                ->where('end_time', $data['end_time'])
                ->get();
            if($checkItems->count() > 0){
                return $this->error('', __("messages.time_slot_already_exists"), 409);
            }
            $item = new TimeSlot($data);
            $item->save();
            return $this->success(data: $item, message: __("messages.item_was_added"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function update(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = TimeSlot::find($id);
            $data = $request->validate([
                "start_time" => ["required", "date_format:H:i"],
                "end_time" => ["required", "date_format:H:i", "after:start_time"],
            ]);
            $checkItems = TimeSlot::where('start_time', $data['start_time'])
                ->where('end_time', $data['end_time'])
                ->get();
            if($checkItems->count() > 0){
                return $this->error('', __("messages.time_slot_already_exists"), 409);
            }
            $item->update($data);
            return $this->success(data: $item, message: __("messages.item_was_updated"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function destroy($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $item = TimeSlot::find($id);
            $item->delete();
            return $this->success(data: [], message: __("messages.item_was_deleted"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }
}
