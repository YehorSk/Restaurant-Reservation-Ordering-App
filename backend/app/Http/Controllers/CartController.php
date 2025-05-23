<?php

namespace App\Http\Controllers;

use App\Models\MenuItem;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class CartController extends Controller
{
    use HttpResponses;

    public function getUserCartItems(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $items = $user->menuItems()->get()->map(function ($item) use ($user) {
                $item->isFavorite = (bool)$user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                return $item;
            });
            return $this->success(data: $items, message: "");
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function addUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $menuItem = MenuItem::where('id', $request->input('menu_item_id'))->first();
            if(!$menuItem->availability){
                return $this->error('', __("messages.item_not_available"), 409);
            }
            $exists = $user->menuItems()
                ->where('menu_item_id', $request->input('menu_item_id'))
                ->first();
            if($exists){
                $user->menuItems()->updateExistingPivot($request->input('menu_item_id'), [
                    'quantity' => $exists->pivot->quantity + $request->input('quantity'),
                    'price' => $exists->pivot->price + $request->input('price')
                ]);
                $newItem = $user->menuItems()->where('menu_item_id', $request->input('menu_item_id'))
                    ->first();

                if ($newItem) {
                    $newItem->isFavorite = (bool) $user->favoriteItems()->where('menu_item_id', $newItem->id)->exists();
                }
                return $this->success(data: [$newItem], message: __("messages.item_in_cart_updated"));
            }
            $user->menuItems()->attach($request->input('menu_item_id'), [
                'quantity' => $request->input('quantity'),
                'price' => $request->input('price')
            ]);
            $newItem = $user->menuItems()->where('menu_item_id', $request->input('menu_item_id'))
                ->first();

            if ($newItem) {
                $newItem->isFavorite = (bool) $user->favoriteItems()->where('menu_item_id', $newItem->id)->exists();
            }
            return $this->success(data: [$newItem], message: __("messages.item_added_to_cart"));
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function deleteUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $exists = $user->menuItems()
                ->wherePivot('menu_item_id', $request->input('menu_item_id'))
                ->first();
            if($exists){
                $item = $user->menuItems()
                    ->wherePivot('menu_item_id', $request->input('menu_item_id'))
                    ->first();

                if ($item) {
                    $item->isFavorite = (bool) $user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                }
                $user->menuItems()->detach($exists);
                return $this->success(data: [$item], message: __("messages.item_in_cart_deleted"));
            }
            return $this->error('', "Item was not deleted", 400);
        }
        return $this->error('', __('messages.no_user'), 401);
    }

    public function updateUserCartItem(Request $request){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $exists = $user->menuItems()
                ->wherePivot('id', $request->input('pivot_id'))
                ->first();
            if($exists){
                $user->menuItems()->updateExistingPivot($exists->id, [
                    'price' => $request->input('price'),
                    'quantity' => $request->input('quantity'),
                ]);
                $item = $user->menuItems()
                    ->wherePivot('id', $request->input('pivot_id'))
                    ->first();

                if ($item) {
                    $item->isFavorite = (bool) $user->favoriteItems()->where('menu_item_id', $item->id)->exists();
                }
                return $this->success(data: [$item], message: __("messages.item_in_cart_updated"));
            }
            return $this->error('', "Item in Cart was not updated", 400);
        }
        return $this->error('', __('messages.no_user'), 401);
    }
}
