<#include "base.ftl">

<#macro title>Профиль - Финансовый Трекер</#macro>

<#macro content>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">

                <!-- Заголовок -->
                <h1 class="mb-4"><i class="bi bi-person-circle"></i> Мой профиль</h1>

                <!-- Сообщения -->
                <#if successMessage??>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle"></i> ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </#if>

                <#if errorMessage??>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-circle"></i> ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </#if>

                <!-- Режим просмотра (по умолчанию) -->
                <#if !editMode?? || !editMode>
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="bi bi-info-circle"></i> Информация профиля</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <h6 class="text-muted">Имя пользователя</h6>
                                <p class="mb-0 lead"><strong>${user.username}</strong></p>
                            </div>
                            <hr>
                            <div class="mb-3">
                                <h6 class="text-muted">Email</h6>
                                <p class="mb-0"><strong>${user.email}</strong></p>
                            </div>
                            <hr>
                            <div class="mb-3">
                                <h6 class="text-muted">Полное имя</h6>
                                <p class="mb-0"><strong>${user.firstName} ${user.lastName}</strong></p>
                            </div>
                            <hr>
                            <div class="alert alert-info mb-3">
                                <i class="bi bi-lightbulb"></i>
                                <strong>Совет:</strong>
                                Регулярно обновляйте информацию в профиле и используйте надежный пароль для безопасности вашего аккаунта.
                            </div>
                            <!-- Кнопка редактирования -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${Application.contextPath}/profile?edit=true" class="btn btn-primary">
                                    <i class="bi bi-pencil-square"></i> Редактировать профиль
                                </a>
                            </div>
                        </div>
                    </div>
                </#if>

                <!-- Режим редактирования (по клику на кнопку) -->
                <#if editMode?? && editMode>
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-warning text-dark">
                            <h5 class="mb-0"><i class="bi bi-pencil-square"></i> Редактировать профиль</h5>
                        </div>
                        <div class="card-body">
                            <form method="post" action="${Application.contextPath}/profile">

                                <!-- Email (только чтение) -->
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email"
                                           value="${user.email}" disabled>
                                    <small class="text-muted d-block mt-1">
                                        <i class="bi bi-info-circle"></i> Email используется как логин и не может быть изменен
                                    </small>
                                </div>

                                <!-- Имя пользователя -->
                                <div class="mb-3">
                                    <label for="username" class="form-label">Имя пользователя</label>
                                    <input type="text" class="form-control" id="username" name="username"
                                           value="${user.username}" required>
                                    <small class="text-muted d-block mt-1">
                                        <i class="bi bi-info-circle"></i> Используется для входа вместо email
                                    </small>
                                </div>

                                <!-- Имя -->
                                <div class="mb-3">
                                    <label for="firstName" class="form-label">Имя</label>
                                    <input type="text" class="form-control" id="firstName" name="firstName"
                                           value="${user.firstName}" required>
                                </div>

                                <!-- Фамилия -->
                                <div class="mb-3">
                                    <label for="lastName" class="form-label">Фамилия</label>
                                    <input type="text" class="form-control" id="lastName" name="lastName"
                                           value="${user.lastName}" required>
                                </div>

                                <!-- Разделитель -->
                                <hr class="my-4">

                                <!-- Новый пароль -->
                                <div class="mb-3">
                                    <label for="newPassword" class="form-label">Новый пароль</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword">
                                    <small class="text-muted d-block mt-1">
                                        <i class="bi bi-info-circle"></i> Оставьте пустым, чтобы не менять пароль
                                    </small>
                                </div>

                                <!-- Подтверждение пароля -->
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Подтвердите пароль</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                                </div>

                                <!-- Кнопки -->
                                <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                                    <a href="${Application.contextPath}/profile" class="btn btn-outline-secondary">
                                        <i class="bi bi-arrow-left"></i> Отмена
                                    </a>
                                    <button type="submit" class="btn btn-warning text-dark">
                                        <i class="bi bi-check-circle"></i> Сохранить изменения
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </#if>

            </div>
        </div>
    </div>

    <!-- Стили -->
    <style>
        .form-control:disabled,
        .form-control[readonly] {
            background-color: #e9ecef;
            opacity: 1;
        }

        .card {
            border: none;
            border-radius: 8px;
        }

        .card-header {
            border-radius: 8px 8px 0 0;
            border-bottom: 1px solid rgba(0, 0, 0, 0.125);
        }

        /*.bg-primary {*/
        /*    background-color: #667eea !important;*/
        /*}*/

        .bg-warning {
            background-color: #ffc107 !important;
        }

        h1 {
            color: #667eea;
            font-weight: 600;
        }

        label {
            font-weight: 500;
            color: #333;
        }

        small {
            display: block !important;
        }

        .lead {
            font-size: 1.25rem;
        }
    </style>
</#macro>
