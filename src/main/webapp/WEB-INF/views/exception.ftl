<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ошибка ${statusCode!''} - Финансовый Трекер</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center align-items-center min-vh-100">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow">
                <div class="card-body text-center p-5">
                    <!-- Иконка ошибки -->
                    <#if statusCode?? && statusCode == 404>
                        <i class="bi bi-search text-warning" style="font-size: 5rem;"></i>
                    <#elseif statusCode?? && statusCode == 403>
                        <i class="bi bi-shield-lock text-danger" style="font-size: 5rem;"></i>
                    <#elseif statusCode?? && statusCode == 500>
                        <i class="bi bi-exclamation-triangle text-danger" style="font-size: 5rem;"></i>
                    <#else>
                        <i class="bi bi-x-circle text-secondary" style="font-size: 5rem;"></i>
                    </#if>

                    <!-- Код ошибки -->
                    <h1 class="display-1 fw-bold text-primary mt-4">
                        ${statusCode!'Ошибка'}
                    </h1>

                    <!-- Заголовок -->
                    <#if statusCode?? && statusCode == 404>
                        <h2 class="h4 mb-3">Страница не найдена</h2>
                        <p class="text-muted">
                            К сожалению, запрашиваемая страница не существует или была перемещена.
                        </p>
                    <#elseif statusCode?? && statusCode == 403>
                        <h2 class="h4 mb-3">Доступ запрещён</h2>
                        <p class="text-muted">
                            У вас нет прав для просмотра этой страницы. Пожалуйста, войдите в систему.
                        </p>
                    <#elseif statusCode?? && statusCode == 500>
                        <h2 class="h4 mb-3">Внутренняя ошибка сервера</h2>
                        <p class="text-muted">
                            Произошла непредвиденная ошибка. Мы уже работаем над её исправлением.
                        </p>
                    <#else>
                        <h2 class="h4 mb-3">Пу пу пу...</h2>
                        <p class="text-muted">
                            Произошла ошибка при обработке вашего запроса.
                        </p>
                    </#if>

                    <!-- URL запроса -->
                    <#if uri?? && uri != ''>
                        <div class="alert alert-light mt-4 text-start">
                            <small class="text-muted">
                                <i class="bi bi-link-45deg"></i>
                                <strong>URL:</strong> ${uri}
                            </small>
                        </div>
                    </#if>

                    <!-- Техническая информация (только для 500) -->
                    <#if message?? && statusCode?? && statusCode == 500>
                        <div class="alert alert-danger mt-3 text-start">
                            <small>
                                <i class="bi bi-bug"></i>
                                <strong>Ошибка:</strong> ${message}
                            </small>
                        </div>
                    </#if>

                    <!-- Кнопки -->
                    <div class="d-grid gap-2 d-md-flex justify-content-md-center mt-4">
                        <a href="${Application.contextPath!''}/" class="btn btn-primary">
                            <i class="bi bi-house-door"></i> На главную
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>