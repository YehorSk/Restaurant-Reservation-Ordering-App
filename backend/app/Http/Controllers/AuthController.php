<?php

namespace App\Http\Controllers;

use App\Http\Requests\LoginUserRequest;
use App\Http\Requests\StoreUserRequest;
use App\Models\Cart;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Auth\Events\PasswordReset;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Password;
use Illuminate\Support\Str;

class AuthController extends Controller
{
    use HttpResponses;

    public function getStats(){
        $user = auth('sanctum')->user();
        if($user instanceof User && $user->role === "admin"){
            $data_all = User::all();
            return $this->success([[
                'data_all' => $data_all->count(),
            ]]);
        }
        return $this->error('', 'No user', 401);
    }

    public function authenticate(Request $request)
    {
        $user = $request->user();
        $token = $request->bearerToken();

        return $this->success(data: [[
            'user' => [
                'name' => $user->name,
                'email' => $user->email,
                'updated_at' => $user->updated_at,
                'created_at' => $user->created_at,
                'email_verified_at' => $user->email_verified_at,
                'id' => $user->id,
                'role' => $user->role,
                'address' => $user->address,
                'phone' => $user->phone,
            ],
            'token' => $token
        ]], message: __("messages.authenticated_successfully"));
    }

    public function login(LoginUserRequest $request){
        $request->validated($request->all());
        if (!Auth::attempt(['email' => $request->email, 'password' => $request->password])) {
            return $this->error((object)[], 'Credentials do not match', 422);
        }

        $user = User::where('email',$request->email)->first();

        return $this->success(data: [[
            'user' => $user,
            'token' => $user->createToken("API Token of " . $user->name)->plainTextToken,
        ]], message: __("messages.logged_in_successfully"));
    }

    public function register(StoreUserRequest $request){
        $request->validated($request->all());
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
        ]);
        return $this->success(data: [[
            'user' => $user,
            'token' => $user->createToken("API Token of " . $user->name)->plainTextToken
        ]], message: __("messages.registered_successfully"));
    }

    public function logout(Request $request){
        Auth::user()->currentAccessToken()->delete();
        return $this->success(data: [], message: __("messages.logged_out_successfully"));
    }
    public function reset_password($token,$email) {
        return redirect()->to('http://localhost:5173/reset-password/' . $token . '?email=' . urlencode($email));
    }

    public function update_password(Request $request) {
        $request->validate([
            'token' => 'required',
            'email' => 'required|email',
            'password' => 'required|min:6|confirmed',
        ]);

        $status = Password::reset(
            $request->only('email', 'password', 'password_confirmation', 'token'),
            function (User $user, string $password) {
                $user->forceFill([
                    'password' => Hash::make($password)
                ])->setRememberToken(Str::random(60));

                $user->save();

                event(new PasswordReset($user));
            }
        );
        return response()->json("Password Updated");
    }

    public function updateProfile(Request $request) {
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $name = $request->input('name');
            $address = $request->input('address');
            $phone = $request->input('phone');
            $user->name = $name;
            $user->address = $address;
            $user->phone = $phone;
            $user->save();
            return $this->success(data: [$user], message: __("messages.profile_updated_successfully"));
        }
        return $this->error('', 'No user', 401);
    }

    public function forgotPassword(Request $request)
    {
        $request->validate(['email' => 'required|email']);

        $status = Password::sendResetLink(
            $request->only('email')
        );

        return $status === Password::RESET_LINK_SENT
            ? response()->json(['status' => __($status)], 200)
            : response()->json(['error' => __($status)], 400);
    }

}
