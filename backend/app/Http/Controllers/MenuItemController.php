<?php

namespace App\Http\Controllers;

use App\Models\MenuItem;
use App\Models\User;
use App\Traits\HttpResponses;
use Illuminate\Http\Request;

class MenuItemController extends Controller
{
    use HttpResponses;

    public function index($id){
        $user = auth('sanctum')->user();
        if($user instanceof User){
            $menuItems = MenuItem::where('menu_id', $id)->get();
            return $this->success(data: $menuItems, message: "");
        }
        return $this->error('', 'No user', 401);
    }

    public function store(Request $request, $menu_id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $validatedData = $request->validate([
                'name' => 'required|string|max:255',
                'short_description' => 'nullable|string',
                'long_description' => 'nullable|string',
                'recipe' => 'nullable|string',
                'picture' => 'nullable|string',
                'price' => 'required|numeric|min:0',
            ]);

            $menuItem = MenuItem::create(array_merge($validatedData, ['menu_id' => $menu_id]));

            return $this->success(data: $menuItem, message: "Menu item added successfully.");
        }

        return $this->error('', 'No user', 401);
    }

    public function update(Request $request, $menu_id, $id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $menuItem = MenuItem::where('menu_id', $menu_id)->where('id', $id)->first();

            if (!$menuItem) {
                return $this->error('', 'Menu item not found', 404);
            }

            // Validate input
            $validatedData = $request->validate([
                'name' => 'required|string|max:255',
                'short_description' => 'nullable|string',
                'long_description' => 'nullable|string',
                'recipe' => 'nullable|string',
                'picture' => 'nullable|string',
                'price' => 'required|numeric|min:0',
            ]);

            $menuItem->update($validatedData);

            return $this->success(data: $menuItem, message: "Menu item updated successfully.");
        }

        return $this->error('', 'No user', 401);
    }

    public function destroy($menu_id, $id)
    {
        $user = auth('sanctum')->user();

        if ($user instanceof User) {
            $menuItem = MenuItem::where('menu_id', $menu_id)->where('id', $id)->first();

            if (!$menuItem) {
                return $this->error('', 'Menu item not found', 404);
            }

            $menuItem->delete();

            return $this->success(message: "Menu item deleted successfully.");
        }

        return $this->error('', 'No user', 401);
    }



}
