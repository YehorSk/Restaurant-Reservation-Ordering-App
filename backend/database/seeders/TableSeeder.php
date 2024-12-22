<?php

namespace Database\Seeders;

use App\Models\Table;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class TableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        for ($i = 1; $i <= 5; $i++) {
            Table::create([
                'number' => $i,
                'capacity' => 4,
            ]);
        }

        for ($i = 6; $i <= 10; $i++) {
            Table::create([
                'number' => $i,
                'capacity' => 2,
            ]);
        }
    }
}
