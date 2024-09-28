<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CartController;
use App\Http\Controllers\MenuController;
use App\Http\Controllers\UserController;
use App\Models\Menu;
use Illuminate\Http\Request;
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
Route::post('/login',[AuthController::class,'login']);
Route::post('/register',[AuthController::class,'register']);

//Protected routes
Route::group(['middleware' => ['auth:sanctum']],function (){
    Route::get('/logout',[AuthController::class,'logout']);
});

Route::apiResource('menu', MenuController::class);
Route::controller(UserController::class)->group(function (){
    Route::get('user-cart-items','getUserCartItems');
    Route::post('add-user-cart-item','addUserCartItem');
})->middleware('auth:sanctum');



