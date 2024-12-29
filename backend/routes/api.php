<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CartController;
use App\Http\Controllers\FavoriteController;
use App\Http\Controllers\MenuController;
use App\Http\Controllers\MenuItemController;
use App\Http\Controllers\OrderController;
use App\Http\Controllers\ReservationController;
use App\Http\Controllers\TableController;
use App\Http\Controllers\UserController;
use App\Models\Menu;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Password;
use Illuminate\Support\Facades\Route;
use Laravel\Sanctum\Http\Controllers\CsrfCookieController;

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

Route::controller(MenuItemController::class)->group(function (){
    Route::get('menuItems/{id}', 'index');
})->middleware('auth:sanctum');

//Protected routes
Route::group(['middleware' => ['auth:sanctum']],function (){
    Route::get('/logout',[AuthController::class,'logout']);
    Route::apiResource('menu', MenuController::class);
});

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

Route::prefix("cart")->controller(CartController::class)->group(function (){
    Route::get('/user','getUserCartItems');
    Route::post('/user/add','addUserCartItem');
    Route::post('/user/delete','deleteUserCartItem');
    Route::post('/user/update','updateUserCartItem');
})->middleware('auth:sanctum');

Route::prefix("order")->controller(OrderController::class)->group(function (){
    Route::get('/user/cartItems','getUserCartItems');
    Route::get('/user/orders','getUserOrders');
    Route::get('/user/orders/{id}','getUserOrderDetails');
    Route::get('/user/orders/cancel/{id}','userCancelOrder');
    Route::get('/user/orders/repeat/{id}','repeatOrder');

    // Waiter
    Route::get('/waiter/orders/confirm/{id}','markOrderAsConfirmed');
    Route::get('/waiter/orders/complete/{id}','markOrderAsCompleted');
    Route::get('/waiter/orders/cancel/{id}','markOrderAsCancelled');
    Route::post('/waiter/order','makeWaiterOrder');

    // Chef
    Route::get('/chef/orders/prepare/{id}','markOrderAsPreparing');
    Route::get('/chef/orders/ready/{id}','markOrderAsReadyForPickup');

    Route::post('/user/pickup','makeUserPickUpOrder');
    Route::post('/user/delivery','makeUserDeliveryOrder');
})->middleware('auth:sanctum');

Route::prefix("favorite")->controller(FavoriteController::class)->group(function (){
    Route::get('/user','getUserFavoriteItems');
    Route::post('/user/add','addUserFavoriteItem');
    Route::post('/user/delete','deleteUserFavoriteItem');
});

Route::prefix("table")->controller(TableController::class)->group(function (){
    Route::get('/waiter/tables','getTables');
});

Route::prefix("reservation")->controller(ReservationController::class)->group(function (){
    Route::post('/user/getTimeSlots','getAvailableTimeSlots');
    Route::post('/user/createReservation','createReservation');
    Route::get('/user/reservations','getReservations');
    Route::get('/user/reservations/{id}','getReservationsDetails');
    Route::get('/user/reservations/cancel/{id}','cancelReservation');
});

Route::get('/sanctum/csrf-cookie', [CsrfCookieController::class, 'show']);

// 0 - Pickup
// 1 - Delivery
// 3 - Reservation
// 4 - Waiter Dine-in
