<!DOCTYPE html>
<html>
<head>
    <title>Liste des Utilisateurs</title>
</head>
<body>
    <h1>Utilisateurs</h1>
    <ul>
        @foreach ($users as $user)
            <li>Username: {{ $user->username }}</li>
            <li>Password: {{ $user->password }}</li>
        @endforeach
    </ul>
</body>
</html>
