<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class UserController extends Controller
{
    use HttpResponses;
    public function index(Request $request){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $search = $request->input('search');
            $users = User::query()
                ->when($search, function ($query, $search) {
                    return $query->where(function ($query) use ($search) {
                        $query->where('name', 'LIKE', "%{$search}%")
                            ->orWhere('email', 'LIKE', "%{$search}%")
                            ->orWhere('role', 'LIKE', "%{$search}%");
                    });
                })
                ->paginate(10);
            return $this->success($users, "");
        }
        return $this->error('', 'No user', 401);
    }

    public function update(Request $request, $id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $user = User::find($id);
            $data = $request->validate([
                "name" => "required",
                "email" => "required",
                "role" => "required",
            ]);
            $user->update($data);
            return $this->success(data: $user, message: "User updated successfully");
        }
        return $this->error('', 'No user', 401);
    }

    public function destroy($id){
        $user = auth('sanctum')->user();
        if ($user instanceof User) {
            $user = User::find($id);
            $user->delete();
            return $this->success(message: "User deleted successfully");
        }
        return $this->error('', 'No user', 401);
    }
}
