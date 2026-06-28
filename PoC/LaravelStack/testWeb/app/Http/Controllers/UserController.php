<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;

class UserController extends Controller
{
    public function showUsers() {
        $users = User::all(); // Récupère tous les utilisateurs
        return view('users', compact('users'));
    }
}

