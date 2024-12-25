<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('reservations', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->unsignedBigInteger("client_id");
            $table->unsignedBigInteger("table_id");
            $table->unsignedBigInteger("time_slot_id");
            $table->integer("party_size");
            $table->date("date");
            $table->string("status");
            $table->string("code");
            $table->foreign("client_id")->references("id")->on("users")->onDelete("cascade");
            $table->foreign("time_slot_id")->references("id")->on("time_slot")->onDelete("cascade");
            $table->foreign("table_id")->references("id")->on("tables")->onDelete("cascade");
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('reservations');
    }
};
