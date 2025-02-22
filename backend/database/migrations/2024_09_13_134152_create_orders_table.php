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
        Schema::create('orders', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->dateTime("completed_at")->nullable();
            $table->unsignedBigInteger("client_id")->nullable();
            $table->unsignedBigInteger("table_id")->nullable();
            $table->unsignedBigInteger("waiter_id")->nullable();
            $table->unsignedBigInteger("reservation_id")->nullable();
            $table->string("code");
            $table->float("price");
            $table->string("status");
            $table->longText("special_request")->nullable();
            $table->longText("phone")->nullable();
            $table->longText("address")->nullable();
            $table->longText("instructions")->nullable();
            $table->integer("order_type")->default(false);
            $table->time("start_time");
            $table->time("end_time");
            $table->foreign("client_id")->references("id")->on("users")->onDelete("cascade");
            $table->foreign("table_id")->references("id")->on("tables")->onDelete("cascade");
            $table->foreign("waiter_id")->references("id")->on("users")->onDelete("cascade");
            $table->foreign("reservation_id")->references("id")->on("reservations")->onDelete("cascade");
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('orders');
    }
};
