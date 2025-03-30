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
        return $this->error('', __('messages.no_user'), 401);
    }

    public function authenticate(Request $request)
    {
        $user = $request->user();
        $token = $request->bearerToken();

        $responseData = [
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
                'country_code' => $user->country_code,
                'language' => $user->language,
            ],
            'token' => $token
        ];

        $response = $this->success(data: [$responseData], message: __("messages.authenticated_successfully"));

        if ($user->role !== "admin") {
            $user->devices()->updateOrCreate([
                'device_id' => $request->device_id,
                'device_token' => $request->fcm_token,
                'device_type' => $request->device_type,
            ]);
        }

        return $response;
    }


    public function login(LoginUserRequest $request){
        $request->validated($request->all());
        if (!Auth::attempt(['email' => $request->email, 'password' => $request->password])) {
            return $this->error((object)[], __("messages.incorrect_credentials"), 422);
        }
        $user = User::where('email',$request->email)->first();
        if($user->role !== "admin"){
            $user->devices()->delete();
            $user->devices()->create([
                'device_id' => $request->device_id,
                'device_token' => $request->fcm_token,
                'device_type' => $request->device_type,
            ]);
        }
        return $this->success(data: [[
            'user' => $user,
            'token' => $user->createToken($user->name)->plainTextToken,
        ]]);
    }

    public function register(StoreUserRequest $request){
        $request->validated($request->all());
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
            'role' => 'user'
        ]);
        $user->devices()->create([
            'device_token' => $request->fcm_token,
            'device_id' => $request->device_id,
            'device_type' => $request->device_type,
        ]);
//        $user->sendEmailVerificationNotification();
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
                'country_code' => $user->country_code,
                'language' => $user->language,
            ],
            'token' => $user->createToken("API Token of " . $user->name)->plainTextToken
        ]], message: __("messages.registered_successfully"));
    }

    public function logout(Request $request){
        $user = auth('sanctum')->user();
        Auth::user()->currentAccessToken()->delete();
        $user->devices->each->delete();
        return $this->success(data: [], message: __("messages.logged_out_successfully"));
    }
    public function reset_password($token,$email) {
        return redirect()->to('https://admin.platea.site/reset-password/' . $token . '?email=' . urlencode($email));
    }

    public function update_password(Request $request) {
        $request->validate([
            'token' => 'required',
            'email' => 'required|email',
            'password' => 'required|min:6|confirmed',
        ]);

        Password::reset(
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
            $countryCode = $request->input('country_code');
            $phone = $request->input('phone');
            $user->name = $name;
            $user->address = $address;
            $user->country_code = $countryCode;
            $user->phone = $phone;
            $user->save();
            return $this->success(data: [$user], message: __("messages.profile_updated_successfully"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function updateLanguage($language) {
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $user->language = $language;
            $user->save();
            return $this->success(data: [$user], message: __("messages.profile_updated_successfully"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function forgotPassword(Request $request)
    {
        $request->validate(['email' => 'required|email']);

        $status = Password::sendResetLink(
            $request->only('email')
        );

        if ($status === Password::RESET_LINK_SENT) {
            return $this->success([""], __("messages.reset_link_sent_successfully"));
        }

        return $this->error([], __("messages.reset_link_failed"), 422);
    }

    public function deleteAccount(Request $request){
        $user = auth('sanctum')->user();
        $user->devices->each->delete();
        if($user instanceof User){
            $user->delete();
            return $this->success([""], __("messages.account_deleted_successfully"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

}
