<?php

namespace App\Traits;

use Exception;
use Google\Auth\ApplicationDefaultCredentials;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Log;

trait FCMNotificationTrait
{
    public function sendFCMNotification($token, $title, $body, $data = []){
        $fcmurl = "https://fcm.googleapis.com/v1/projects/platea-75c01/messages:send";

        $notification = [
            'message' => [
                'token' => $token,
                'notification' => [
                    'title' => $title,
                    'body' => $body,
                ],
                'data' => !empty($data) ? $data : null,
            ],
        ];

        try {
            $accessToken = $this->getAccessToken();

            $response = Http::withHeaders([
                'Authorization' => 'Bearer ' . $accessToken,
                'Content-Type' => 'application/json',
            ])->post($fcmurl, $notification);

            if ($response->failed()) {
                Log::error('FCM request failed', [
                    'status' => $response->status(),
                    'body' => $response->body(),
                ]);
                return false;
            }

            return $response->json();
        } catch (Exception $e) {
            Log::error('Error sending notification: ' . $e->getMessage());
            return false;
        }
    }

    private function getAccessToken(){
        $keyPath = config('services.firebase.key_path');

        if (!file_exists($keyPath)) {
            Log::error('Firebase service account JSON file not found.');
            throw new \Exception('Service account JSON file not found.');
        }

        putenv('GOOGLE_APPLICATION_CREDENTIALS=' . $keyPath);

        $scopes = ['https://www.googleapis.com/auth/firebase.messaging'];

        $credentials = ApplicationDefaultCredentials::getCredentials($scopes);
        $token = $credentials->fetchAuthToken();

        if (empty($token['access_token'])) {
            Log::error('Failed to retrieve access token.');
            throw new \Exception('Unable to fetch access token for Firebase.');
        }

        return $token['access_token'];
    }
}
