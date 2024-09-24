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
        Schema::create('cart_menu_item', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->unsignedBigInteger("cart_id");
            $table->unsignedBigInteger("menu_item_id");
            $table->integer("quantity");
            $table->float("price");

            $table->foreign("cart_id")->references("id")->on("carts")->onDelete("cascade");
            $table->foreign("menu_item_id")->references("id")->on("menu_items")->onDelete("cascade");
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('cart_menu_item');
    }
};
