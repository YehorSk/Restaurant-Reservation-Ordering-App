<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CartController;
use App\Http\Controllers\FavoriteController;
use App\Http\Controllers\MenuController;
use App\Http\Controllers\UserController;
use App\Models\Menu;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Password;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    $user = $request->user();

    $token = $request->bearerToken();

    // Construct the custom response
    return response()->json([
        'status' => 200,
        'message' => null,
        'data' => [
            'user' => [
                'name' => $user->name,
                'email' => $user->email,
                'updated_at' => $user->updated_at,
                'created_at' => $user->created_at,
                'id' => $user->id,
                'role' => $user->role,
            ],
            'token' => $token
        ]
    ]);
})->middleware('auth:sanctum');

//Public routes
Route::controller(AuthController::class)->group(function (){
    Route::post('/login', 'login');
    Route::post('/register', 'register');
    Route::get('/reset-password/{token}/{email}','reset_password')->name('password.reset');
    Route::post('/update-password', 'update_password');
});

//Protected routes
Route::group(['middleware' => ['auth:sanctum']],function (){
    Route::get('/logout',[AuthController::class,'logout']);
});

Route::apiResource('menu', MenuController::class);

Route::post('/forgot-password', function (Request $request) {
    $request->validate(['email' => 'required|email']);

    $status = Password::sendResetLink(
        $request->only('email')
    );

    return $status === Password::RESET_LINK_SENT
        ? back()->with(['status' => __($status)])
        : back()->withErrors(['email' => __($status)]);
})->name('password.email');

Route::group(['middleware' => ['auth:sanctum']], function () {
    Route::post('/logout', [AuthController::class, 'logout']);
});

Route::controller(CartController::class)->group(function (){
    Route::get('user-cart-items','getUserCartItems');
    Route::post('add-user-cart-item','addUserCartItem');
    Route::post('delete-user-cart-item','deleteUserCartItem');
    Route::post('update-user-cart-item','updateUserCartItem');
})->middleware('auth:sanctum');

Route::controller(FavoriteController::class)->group(function (){
    Route::get('user-favorite-items','getUserFavoriteItems');
    Route::post('add-user-favorite-item','addUserFavoriteItem');
    Route::post('delete-user-favorite-item','deleteUserFavoriteItem');
});

