<?php

namespace Database\Seeders;

use App\Models\TimeSlot;
use Carbon\Carbon;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class TimeSlotSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $startTime = Carbon::createFromTime(8, 0);
        $endTime = Carbon::createFromTime(21, 0);

        // Loop to generate time slots
        while ($startTime->lessThan($endTime)) {
            TimeSlot::create([
                'start_time' => $startTime->format('H:i:s'),
                'end_time' => $startTime->copy()->addHour()->format('H:i:s'),
            ]);

            // Increment by 15 minutes
            $startTime->addMinutes(15);
        }
    }
}
