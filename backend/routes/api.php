<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CartController;
use App\Http\Controllers\FavoriteController;
use App\Http\Controllers\LocaleController;
use App\Http\Controllers\MenuController;
use App\Http\Controllers\MenuItemController;
use App\Http\Controllers\OrderController;
use App\Http\Controllers\ReservationController;
use App\Http\Controllers\TableController;
use App\Http\Controllers\TimeSlotController;
use App\Http\Controllers\UserController;
use App\Http\Middleware\Localization;
use Illuminate\Support\Facades\Route;
use Laravel\Sanctum\Http\Controllers\CsrfCookieController;

Route::get('/sanctum/csrf-cookie', [CsrfCookieController::class, 'show']);

Route::get('/locale/{locale}', [LocaleController::class, 'setLocale']);

//Public routes
Route::controller(AuthController::class)->group(function () {
    Route::post('/login', 'login');
    Route::post('/register', 'register');
    Route::get('/reset-password/{token}/{email}', 'reset_password')->name('password.reset');
    Route::post('/update-password', 'update_password');
    Route::post('/update-profile', 'updateProfile');

    Route::get('/users/stats','getStats');
});

//Protected routes
Route::group(['middleware' => ['auth:sanctum']],function (){
    Route::get('/logout',[AuthController::class,'logout']);
    Route::get('/forgot-password',[AuthController::class,'forgotPassword']);
    Route::get('/user', [AuthController::class, 'authenticate']);
    Route::post('/logout', [AuthController::class, 'logout']);
    Route::apiResource('menu', MenuController::class);
    Route::apiResource('timeSlots', TimeSlotController::class);
    Route::apiResource('tables', TableController::class);
    Route::apiResource('users', UserController::class);

    Route::group(['prefix' => 'menuItems/{id}'], function () {
        Route::apiResource('items', MenuItemController::class);
    });

    Route::get('/menuItems/stats', [MenuItemController::class, 'getStats']);

    Route::prefix("cart")->controller(CartController::class)->group(function (){
        Route::get('/user','getUserCartItems');
        Route::post('/user/add','addUserCartItem');
        Route::post('/user/delete','deleteUserCartItem');
        Route::post('/user/update','updateUserCartItem');
    });

    Route::prefix("order")->controller(OrderController::class)->group(function (){
        //Admin
        Route::get('/admin/getAllOrders','getAllOrders');
        Route::put('/admin/updateOrder/{id}','adminUpdateOrder');
        Route::delete('/admin/deleteOrder/{id}','adminDeleteOrder');

        // Waiter
        Route::get('/waiter/orders/confirm/{id}','markOrderAsConfirmed');
        Route::get('/waiter/orders/complete/{id}','markOrderAsCompleted');
        Route::get('/waiter/orders/cancel/{id}','markOrderAsCancelled');
        Route::post('/waiter/order','makeWaiterOrder');

        // Chef
        Route::get('/chef/orders/prepare/{id}','markOrderAsPreparing');
        Route::get('/chef/orders/ready/{id}','markOrderAsReadyForPickup');

        //User
        Route::post('/user/pickup','makeUserPickUpOrder');
        Route::post('/user/delivery','makeUserDeliveryOrder');
        Route::get('/user/cartItems','getUserCartItems');
        Route::get('/user/orders','getUserOrders');
        Route::get('/user/orders/{id}','getUserOrderDetails');
        Route::get('/user/orders/cancel/{id}','userCancelOrder');
        Route::get('/user/orders/repeat/{id}','repeatOrder');

        Route::get('/stats/{year}','getStats');
    });

    Route::prefix("favorite")->controller(FavoriteController::class)->group(function (){
        Route::get('/user','getUserFavoriteItems');
        Route::post('/user/add','addUserFavoriteItem');
        Route::post('/user/delete','deleteUserFavoriteItem');
    });

    Route::prefix("table")->controller(TableController::class)->group(function (){
        Route::get('/waiter/tables','getTables');
    });

    Route::prefix("reservation")->controller(ReservationController::class)->group(function (){
        //Admin
        Route::get('/admin/getAllReservations','getAllReservations');
        Route::put('/admin/updateReservation/{id}','adminUpdateReservation');
        Route::delete('/admin/deleteReservation/{id}','adminDeleteReservation');

        //User
        Route::post('/user/getTimeSlots','getAvailableTimeSlots');
        Route::post('/user/getTimeSlotsTest','getAvailableTimeSlotsTest');
        Route::post('/user/createReservation','createReservation');
        Route::get('/user/reservations','getReservations');
        Route::get('/user/reservations/{id}','getReservationsDetails');
        Route::get('/user/reservations/cancel/{id}','cancelReservation');

        Route::get('/stats/{year}','getStats');
    });



});
