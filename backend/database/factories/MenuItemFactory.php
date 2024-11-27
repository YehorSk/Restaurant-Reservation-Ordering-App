<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\MenuItem>
 */
class MenuItemFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'menu_id' => null, // To be assigned dynamically
            'name' => $this->faker->unique()->words(3, true), // Unique menu item name
            'short_description' => $this->faker->sentence(10), // Short description
            'long_description' => $this->faker->paragraph(3), // Long description
            'recipe' => $this->faker->paragraph(2), // Recipe details
            'picture' => $this->faker->imageUrl(640, 480, 'food', true, 'Food'), // Random food image
            'price' => $this->faker->randomFloat(2, 5, 50), // Price between 5 and 50
        ];
    }
}
