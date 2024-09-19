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

            $table->unsignedBigInteger("menu_id");
            $table->unsignedBigInteger("menu_item_id");

            $table->foreign("menu_id")->references("id")->on("menus")->onDelete("cascade");
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
