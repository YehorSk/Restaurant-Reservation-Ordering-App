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
            $table->unsignedBigInteger("client_id");
            $table->unsignedBigInteger("table_id")->nullable();
            $table->unsignedBigInteger("waiter_id")->nullable();
            $table->unsignedBigInteger("reservation_id")->nullable();
            $table->float("price");
            $table->string("status");
            $table->longText("special_request");
//            $table->boolean("pickup")->default(false);
//            $table->boolean("home_delivery")->default(false);
//            $table->boolean("dine_in")->default(false);
            $table->integer("order_type")->default(false);
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
