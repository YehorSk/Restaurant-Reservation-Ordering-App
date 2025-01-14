<?php

namespace App\Http\Controllers;

use App\Traits\HttpResponses;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\App;
use Illuminate\Support\Facades\Session;

class LocaleController extends Controller
{

    use HttpResponses;

    public function setLocale($locale){
        if (in_array($locale, ['en', 'uk', 'sk'])) {
            App::setLocale($locale);
            Session::put('locale', $locale);
        } else {
            App::setLocale('en');
            Session::put('locale', 'en');
        }
        return $this->success(data: [""], message: __("messages.language_changed_successfully"));
    }

}
