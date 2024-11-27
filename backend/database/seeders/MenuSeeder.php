<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class MenuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $menus = [
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Breakfast Special', 'description' => 'Delicious breakfast options.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Lunch Special', 'description' => 'Hearty lunch dishes.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Dinner Delight', 'description' => 'Sumptuous dinner offerings.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Vegan Feast', 'description' => 'Tasty vegan-friendly meals.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Desserts', 'description' => 'Sweet treats for everyone.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Appetizers', 'description' => 'Perfect starters for your meal.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Beverages', 'description' => 'Refreshing drinks.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Kids Menu', 'description' => 'Fun and healthy options for kids.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Seasonal Specials', 'description' => 'Dishes available for a limited time.', 'availability' => true],
            ['created_at' => '2024-11-08 12:55:28', 'updated_at' => '2024-11-08 12:55:28', 'name' => 'Chef’s Choice', 'description' => 'Signature dishes crafted by our chef.', 'availability' => true],
        ];

        DB::table('menus')->insert($menus);
    }
}
