<#include "base.ftl">

<#macro title>Вход - Финансовый Трекер</#macro>

<#macro content>
    <div class="row justify-content-center mt-5">
        <div class="col-md-5">
            <div class="card shadow">
                <div class="card-body p-5">
                    <div class="text-center mb-4">
                        <i class="bi bi-cash-coin text-primary" style="font-size: 3rem;"></i>
                        <h2 class="mt-2">Финансовый Трекер</h2>
                        <p class="text-muted">Войдите в свой аккаунт</p>
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

                    <form method="post" action="${Application.contextPath}/login">
                        <div class="mb-3">
                            <label for="username" class="form-label">Имя пользователя или почта</label>
                            <input type="text" class="form-control" id="usernameOrEmail" name="usernameOrEmail"
                                   value="${usernameOrEmail!''}" required autocomplete="usernameOrEmail"
                                   placeholder="Введите имя пользователя">
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Пароль</label>
                            <input type="password" class="form-control" id="password" name="password"
                                   required autocomplete="current-password"
                                   placeholder="Введите пароль">
                        </div>

                        <button type="submit" class="btn btn-primary w-100 py-2">
                            <i class="bi bi-box-arrow-in-right"></i> Войти
                        </button>
                    </form>

                    <hr class="my-4">

                    <p class="text-center mb-0">
                        Нет аккаунта?
                        <a href="${Application.contextPath}/register" class="text-decoration-none">
                            Зарегистрироваться
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</#macro>