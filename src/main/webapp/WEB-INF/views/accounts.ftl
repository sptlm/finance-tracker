<#include "base.ftl">

<#macro title>Мои счёта - Финансовый Трекер</#macro>

<#macro content>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1><i class="bi bi-wallet2"></i> Мои счёта</h1>
        <button class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#createModal">
            <i class="bi bi-plus-circle"></i> Добавить счёт
        </button>
    </div>

    <!-- Общий баланс -->
    <#if totalBalance??>
        <div class="card mb-4 border-0 shadow-sm" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
            <div class="card-body">
                <h6 class="mb-2" style="opacity: 0.9;">Общий баланс всех счётов</h6>
                <h2 class="mb-0">
                    <#if totalBalance gt 0>
                        +${totalBalance?string('0.00')} ₽
                    <#else>
                        ${totalBalance?string('0.00')} ₽
                    </#if>
                </h2>
                <p class="mb-0 mt-2 small" style="opacity: 0.9;">В рублях</p>
            </div>
        </div>
    </#if>

    <!-- Таблица счётов -->
    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <#if accounts?has_content>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>Название</th>
                            <th>Валюта</th>
                            <th class="text-end">Начальный баланс</th>
                            <th class="text-end">Текущий баланс</th>
                            <#if totalBalance??>
                                <th class="text-end">В рублях</th>
                            </#if>
                            <th class="text-center" style="width: 150px;">Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list accounts as account>
                            <tr data-account-id="${account.id?c}">
                                <td>
                                    <i class="bi bi-wallet2" style="color: #667eea;"></i>
                                    <strong>${account.name}</strong>
                                </td>
                                <td>
                                    <#-- Проверяем наличие currency объекта или используем старое поле -->
                                    <#if account.currency??>
                                        <span class="badge bg-info text-dark">
                                            ${account.currencySymbol!account.currency.symbol!''} | ${account.currency.code!''} | ${account.currency.name!''}
                                        </span>
                                    <#elseif account.getCurrency??>
                                        <span class="badge bg-info text-dark">
                                            ${account.getCurrency().getSymbol()!''} | ${account.getCurrency().getCode()!''} | ${account.currency.name!''}
                                        </span>
                                    <#else>
                                        <span class="badge bg-secondary">
                                            N/A
                                        </span>
                                    </#if>
                                </td>
                                <td class="text-end">
                                    ${account.initialBalance?string('0.00')}
                                </td>
                                <td class="text-end">
                                    <strong>
                                        <#if account.currentBalance gt 0>
                                            <span style="color: #10b981;">${account.currentBalance?string('0.00')}${account.currency.symbol!''}</span>
                                        <#else>
                                            <span style="color: #ef4444;">${account.currentBalance?string('0.00')}${account.currency.symbol!''}</span>
                                        </#if>
                                    </strong>
                                </td>
                                <#if totalBalance??>
                                    <td class="text-end">
                                        <#-- Проверяем наличие метода конвертации -->
                                        <#if account.getCurrentBalanceInRub??>
                                            <strong>
                                                <#assign rubBalance = account.getCurrentBalanceInRub()>
                                                <#if rubBalance gt 0>
                                                    <span style="color: #10b981;">${rubBalance?string('0.00')}₽</span>
                                                <#else>
                                                    <span style="color: #ef4444;">${rubBalance?string('0.00')}₽</span>
                                                </#if>
                                            </strong>
                                        <#else>
                                            <span class="text-muted">-</span>
                                        </#if>
                                    </td>
                                </#if>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-primary"
                                            data-bs-toggle="modal"
                                            data-bs-target="#editModal"
                                            onclick="loadAccountForEdit(${account.id?c})"
                                            title="Редактировать">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <form method="post" action="${Application.contextPath}/accounts"
                                          style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${account.id?c}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Вы уверены? Это действие нельзя отменить.')"
                                                title="Удалить">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            <#else>
                <div class="text-center py-5">
                    <i class="bi bi-inbox text-muted" style="font-size: 3rem;"></i>
                    <p class="text-muted mt-3">
                        У вас пока нет счётов.
                        <a href="#" data-bs-toggle="modal" data-bs-target="#createModal">Создайте первый!</a>
                    </p>
                </div>
            </#if>
        </div>
    </div>

    <!-- МОДАЛЬНОЕ ОКНО: СОЗДАНИЕ СЧЕТА -->
    <div class="modal fade" id="createModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white border-0">
                    <h5 class="modal-title">
                        <i class="bi bi-plus-circle"></i> Создать новый счёт
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>

                <form method="post" action="${Application.contextPath}/accounts" id="createForm">
                    <input type="hidden" name="action" value="create">

                    <div class="modal-body">
                        <!-- Название счета -->
                        <div class="mb-3">
                            <label for="createName" class="form-label">
                                <i class="bi bi-tag"></i> Название счета <span class="text-danger">*</span>
                            </label>
                            <input type="text" id="createName" name="name" class="form-control"
                                   placeholder="Например: Основной счет, Сбережения" required>
                        </div>

                        <!-- Выбор валюты -->
                        <div class="mb-3">
                            <label for="createCurrency" class="form-label">
                                <i class="bi bi-currency-dollar"></i> Валюта <span class="text-danger">*</span>
                            </label>
                            <#-- Проверяем наличие списка валют -->
                            <#if currencies?? && currencies?has_content>
                                <select id="createCurrency" name="currencyId" class="form-select" required>
                                    <option value="">-- Выберите валюту --</option>
                                    <#list currencies as curr>
                                        <option value="${curr.id?c}">
                                            ${curr.symbol} ${curr.code} - ${curr.name}
                                        </option>
                                    </#list>
                                </select>
                            <#else>
                            <#-- Fallback на старый способ если currencies не загружены -->
                                <select id="createCurrency" name="currency" class="form-select" required>
                                    <option value="">-- Выберите валюту --</option>
                                    <option value="RUB">₽ RUB - Российский рубль</option>
                                    <option value="USD">$ USD - Доллар США</option>
                                    <option value="EUR">€ EUR - Евро</option>
                                </select>
                            </#if>
                        </div>

                        <!-- Начальный баланс -->
                        <div class="mb-3">
                            <label for="createBalance" class="form-label">
                                <i class="bi bi-cash"></i> Начальный баланс <span class="text-danger">*</span>
                            </label>
                            <input type="number" id="createBalance" name="initialBalance" class="form-control"
                                   placeholder="0.00" min="0" step="0.01" required>
                        </div>
                    </div>

                    <div class="modal-footer bg-light border-top">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="bi bi-x"></i> Отмена
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check2"></i> Создать
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- МОДАЛЬНОЕ ОКНО: РЕДАКТИРОВАНИЕ СЧЕТА -->
    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-warning text-dark border-0">
                    <h5 class="modal-title">
                        <i class="bi bi-pencil-square"></i> Редактировать счёт
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <form method="post" action="${Application.contextPath}/accounts" id="editForm">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" id="editId">

                    <div class="modal-body">
                        <!-- Название счета -->
                        <div class="mb-3">
                            <label for="editName" class="form-label">
                                <i class="bi bi-tag"></i> Название счета <span class="text-danger">*</span>
                            </label>
                            <input type="text" id="editName" name="name" class="form-control" required>
                        </div>

                        <!-- Выбор валюты -->
                        <div class="mb-3">
                            <label for="editCurrency" class="form-label">
                                <i class="bi bi-currency-dollar"></i> Валюта <span class="text-danger">*</span>
                            </label>
                            <#if currencies?? && currencies?has_content>
                                <select id="editCurrency" name="currencyId" class="form-select" required>
                                    <option value="">-- Выберите валюту --</option>
                                    <#list currencies as curr>
                                        <option value="${curr.id?c}">
                                            ${curr.symbol} ${curr.code} - ${curr.name}
                                        </option>
                                    </#list>
                                </select>
                            <#else>
                                <select id="editCurrency" name="currency" class="form-select" required>
                                    <option value="">-- Выберите валюту --</option>
                                    <option value="RUB">₽ RUB - Российский рубль</option>
                                    <option value="USD">$ USD - Доллар США</option>
                                    <option value="EUR">€ EUR - Евро</option>
                                </select>
                            </#if>
                        </div>

                        <!-- Информация о балансе (только для чтения) -->
                        <div class="mb-3">
                            <label for="editBalance" class="form-label">
                                <i class="bi bi-cash"></i> Текущий баланс (только для чтения)
                            </label>
                            <input type="text" id="editBalance" class="form-control" readonly>
                        </div>
                    </div>

                    <div class="modal-footer bg-light border-top">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="bi bi-x"></i> Отмена
                        </button>
                        <button type="submit" class="btn btn-warning">
                            <i class="bi bi-check2"></i> Сохранить
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Стили -->
    <style>
        .table-hover tbody tr:hover {
            background-color: rgba(102, 126, 234, 0.05);
            transition: background-color 0.15s ease-in-out;
        }

        .card {
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1) !important;
        }

        .form-label {
            font-weight: 500;
            color: #333;
        }

        .modal-header {
            border-radius: 8px 8px 0 0;
        }
    </style>

    <!-- JavaScript -->
    <script>
        // Данные аккаунтов для редактирования
        const accountsData = {
            <#if accounts?has_content>
            <#list accounts as acc>
            ${acc.id?c}: {
                name: '${acc.name?js_string}',
                <#-- Проверяем какое поле использовать -->
                <#if acc.currencyId??>
                currencyId: ${acc.currencyId?c},
                </#if>
                <#if acc.currency?? && !acc.currencyId??>
                currency: '${acc.currency?js_string}',
                </#if>
                balance: '${acc.currentBalance?string('0.00')}'
            }<#if acc_has_next>,</#if>
            </#list>
            </#if>
        };

        /**
         * Загружает данные аккаунта для редактирования
         */
        function loadAccountForEdit(accountId) {
            const data = accountsData[accountId];
            if (!data) {
                alert('Ошибка: счет не найден');
                return;
            }

            document.getElementById('editId').value = accountId;
            document.getElementById('editName').value = data.name;

            // Устанавливаем валюту (поддерживаем оба формата)
            const currencySelect = document.getElementById('editCurrency');
            if (data.currencyId) {
                currencySelect.value = data.currencyId;
            } else if (data.currency) {
                currencySelect.value = data.currency;
            }

            document.getElementById('editBalance').value = data.balance;
        }

        /**
         * Инициализация при загрузке страницы
         */
        document.addEventListener('DOMContentLoaded', function() {
            // Обработчик закрытия модального окна создания
            const createModal = document.getElementById('createModal');
            if (createModal) {
                createModal.addEventListener('hidden.bs.modal', function() {
                    document.getElementById('createForm').reset();
                });
            }

            // Валидация формы создания
            const createForm = document.getElementById('createForm');
            if (createForm) {
                createForm.addEventListener('submit', function(e) {
                    const name = document.getElementById('createName').value.trim();
                    const currency = document.getElementById('createCurrency').value;
                    const balance = document.getElementById('createBalance').value;

                    if (!name || !currency || !balance) {
                        e.preventDefault();
                        alert('Пожалуйста, заполните все обязательные поля');
                    }
                });
            }

            // Валидация формы редактирования
            const editForm = document.getElementById('editForm');
            if (editForm) {
                editForm.addEventListener('submit', function(e) {
                    const name = document.getElementById('editName').value.trim();
                    const currency = document.getElementById('editCurrency').value;

                    if (!name || !currency) {
                        e.preventDefault();
                        alert('Пожалуйста, заполните все обязательные поля');
                    }
                });
            }
        });
    </script>
</#macro>
