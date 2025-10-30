
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><@title></@title></title>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- css -->
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .main-content {
            flex: 1;
        }
        .navbar-brand {
            font-weight: bold;
            font-size: 1.5rem;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 20px 0;
            margin-top: auto;
        }
        .stat-card {
            transition: transform 0.2s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .income {
            color: #198754;
        }
        .expense {
            color: #dc3545;
        }
        .badge-income {
            background-color: #198754;
        }
        .badge-expense {
            background-color: #dc3545;
        }
    </style>
</head>

<body>
<#if Session.user??>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="${Application.contextPath}/dashboard">
                <i class="bi bi-cash-coin"></i> Финансовый Трекер
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${Application.contextPath}/dashboard">
                            <i class="bi bi-house-door"></i> Дашборд
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${Application.contextPath}/accounts">
                            <i class="bi bi-wallet2"></i> Счета
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${Application.contextPath}/transactions">
                            <i class="bi bi-arrow-left-right"></i> Транзакции
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${Application.contextPath}/categories">
                            <i class="bi bi-grid"></i> Категории
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${Application.contextPath}/tags">
                            <i class="bi bi-tags"></i> Теги
                        </a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userDropdown"
                           role="button" data-bs-toggle="dropdown">
                            <i class="bi bi-person-circle"></i> ${Session.user.username}
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li>
                                <a class="dropdown-item" href="${Application.contextPath}/profile">
                                    <i class="bi bi-gear"></i> Профиль
                                </a>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item text-danger" href="${Application.contextPath}/logout">
                                    <i class="bi bi-box-arrow-right"></i> Выход
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</#if>

<#-- Основной контент -->
<main class="main-content">
    <div class="container mt-4">
        <#-- Flash сообщения -->
        <#if Session.successMessage??>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> ${Session.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </#if>

        <#if Session.errorMessage??>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle"></i> ${Session.errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </#if>

        <#-- Основной контент -->
        <div class="content">
            <@content></@content>
        </div>
    </div>
</main>

<#-- footer -->
<footer class="footer mt-5">
    <div class="container text-center">
        <p class="text-muted mb-0">
            &copy; 2025 Финансовый Трекер. Все права защищены.
        </p>
    </div>
</footer>


</body>
</html>
