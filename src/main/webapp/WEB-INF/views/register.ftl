<#include "base.ftl">

<#macro title>Регистрация - Финансовый Трекер</#macro>

<#macro content>
    <div class="row justify-content-center mt-4">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-body p-4">
                    <div class="text-center mb-4">
                        <i class="bi bi-person-plus text-primary" style="font-size: 3rem;"></i>
                        <h2 class="mt-2">Регистрация</h2>
                        <p class="text-muted">Создайте новый аккаунт</p>
                    </div>

                    <#if errorMessage??>
                        <div class="alert alert-danger">
                            <i class="bi bi-exclamation-circle"></i> ${errorMessage}
                        </div>
                    </#if>

                    <#if successMessage??>
                        <div class="alert alert-success">
                            <i class="bi bi-check-circle"></i> ${successMessage}
                        </div>
                    </#if>

                    <form method="post" action="${Application.contextPath}/register">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="username" class="form-label">
                                    Имя пользователя <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="username" name="username"
                                       value="${username!''}" required
                                       placeholder="3-50 символов">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="email" class="form-label">
                                    Email <span class="text-danger">*</span>
                                </label>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="${email!''}" required
                                       placeholder="your@email.com">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="firstName" class="form-label">Имя</label>
                                <input type="text" class="form-control" id="firstName" name="firstName"
                                       value="${firstName!''}"
                                       placeholder="Ваше имя">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="lastName" class="form-label">Фамилия</label>
                                <input type="text" class="form-control" id="lastName" name="lastName"
                                       value="${lastName!''}"
                                       placeholder="Ваша фамилия">
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">
                                Пароль <span class="text-danger">*</span>
                            </label>
                            <input type="password" class="form-control" id="password" name="password"
                                   required placeholder="Минимум 8 символов">
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">
                                Подтвердите пароль <span class="text-danger">*</span>
                            </label>
                            <input type="password" class="form-control" id="confirmPassword"
                                   name="confirmPassword" required
                                   placeholder="Повторите пароль">
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2">
                            <i class="bi bi-person-check"></i> Зарегистрироваться
                        </button>
                    </form>

                    <hr class="my-4">

                    <p class="text-center mb-0">
                        Уже есть аккаунт?
                        <a href="${Application.contextPath}/login" class="text-decoration-none">
                            Войти
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</#macro>