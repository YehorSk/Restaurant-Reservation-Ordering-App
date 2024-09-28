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
        Schema::create('menu_items_orders', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->integer("quantity");
            $table->float("price");
            $table->unsignedBigInteger("order_id");
            $table->unsignedBigInteger("menu_item_id");

            $table->foreign("order_id")->references("id")->on("orders")->onDelete("cascade");
            $table->foreign("menu_item_id")->references("id")->on("menu_items")->onDelete("cascade");
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('menu_items_orders');
    }
};
