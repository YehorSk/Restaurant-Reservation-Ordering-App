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

    public function login(LoginUserRequest $request){
        $request->validated($request->all());
        if (!Auth::attempt(['email' => $request->email, 'password' => $request->password])) {
            return $this->error((object)[], 'Credentials do not match', 401);
        }

        $user = User::where('email',$request->email)->first();

        return $this->success([
            'user' => $user,
            'token' => $user->createToken("API Token of " . $user->name)->plainTextToken,
            'message' => 'Logged in successfully'
        ]);
    }

    public function register(StoreUserRequest $request){
        $request->validated($request->all());
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
        ]);
        return $this->success([
            'user' => $user,
            'token' => $user->createToken("API Token of " . $user->name)->plainTextToken
        ]);
    }

    public function logout(Request $request){
        Auth::user()->currentAccessToken()->delete();
        return $this->success([
            'message' => 'Logged out successfully'
        ]);
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
}
