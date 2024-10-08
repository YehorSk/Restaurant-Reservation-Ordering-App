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
        Schema::create('favorites_menu_items', function (Blueprint $table) {
            $table->id();
            $table->timestamps();

            $table->unsignedBigInteger("favorite_id");
            $table->unsignedBigInteger("menu_item_id");

            $table->foreign("favorite_id")->references("id")->on("favorites")->onDelete("cascade");
            $table->foreign("menu_item_id")->references("id")->on("menu_items")->onDelete("cascade");

        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('favorites_menu_items');
    }
};
