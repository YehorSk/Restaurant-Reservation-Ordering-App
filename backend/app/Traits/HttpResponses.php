<?php
namespace App\Traits;

trait HttpResponses
{
    protected function success($data= [],$message = null,$code = 200, $textStatus = "")
    {
        return response()->json([
            'status' => $code,
            'text_status' => $textStatus,
            'message' => $message,
            'data' => $data,
        ], $code);
    }

    protected function error($data= [],$message = null,$code, $textStatus = "")
    {
        return response()->json([
            'status' => $code,
            'text_status' => $textStatus,
            'message' => $message,
            'data' => $data,
        ], $code);
    }
}
?>
