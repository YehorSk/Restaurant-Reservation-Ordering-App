<?php

namespace Database\Seeders;

use App\Models\Menu;
use App\Models\MenuItem;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class MenuItemSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $menus = Menu::all();

        // Loop through each menu and generate 5 items for each
        foreach ($menus as $menu) {
            MenuItem::factory()
                ->count(5) // Generate 5 items per menu
                ->create([
                    'menu_id' => $menu->id, // Assign the menu_id dynamically
                ]);
        }
    }
}
