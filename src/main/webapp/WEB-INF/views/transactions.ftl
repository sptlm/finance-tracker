<#include "base.ftl">

<#macro title>Транзакции - Финансовый Трекер</#macro>

<#macro content>
<#-- Безопасные алиасы для фильтров -->
    <#assign currentTypeSafe = (currentType!'')>
    <#assign currentCategorySafe = (currentCategory!'')>
    <#assign dateFromSafe = (dateFrom!'')>
    <#assign dateToSafe = (dateTo!'')>
    <#assign amountFromSafe = (amountFrom!'')>
    <#assign amountToSafe = (amountTo!'')>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1><i class="bi bi-arrow-left-right"></i> Транзакции</h1>
        <button class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#createModal">
            <i class="bi bi-plus-circle"></i> Добавить транзакцию
        </button>
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

    <!-- Табы фильтров -->
    <ul class="nav nav-tabs mb-3">
        <li class="nav-item">
            <a class="nav-link ${(currentTypeSafe == '')?then('active','')}"
               href="${Application.contextPath}/transactions">
                Все
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${(currentTypeSafe == 'INCOME')?then('active','')}"
               href="${Application.contextPath}/transactions?type=INCOME">
                <i class="bi bi-arrow-up-circle text-success"></i> Доходы
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${(currentTypeSafe == 'EXPENSE')?then('active','')}"
               href="${Application.contextPath}/transactions?type=EXPENSE">
                <i class="bi bi-arrow-down-circle text-danger"></i> Расходы
            </a>
        </li>
    </ul>

    <!-- Панель фильтров -->
    <div class="card mb-4 border-0 shadow-sm">
        <div class="card-header bg-light border-bottom">
            <h5 class="mb-0"><i class="bi bi-funnel"></i> Фильтры</h5>
        </div>
        <div class="card-body">
            <form method="get" action="${Application.contextPath}/transactions">
                <input type="hidden" name="type" value="${currentTypeSafe}">

                <div class="row g-3">
                    <div class="col-md-3">
                        <label for="category" class="form-label">Категория</label>
                        <select id="category" name="category" class="form-select">
                            <option value="">Все категории</option>
                            <#if categories?has_content>
                                <#list categories as cat>
                                    <option value="${cat.id}"
                                            ${(currentCategorySafe == cat.id?string)?then('selected','')}>
                                        ${cat.name}
                                    </option>
                                </#list>
                            </#if>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="dateFrom" class="form-label">Дата с</label>
                        <input type="date" id="dateFrom" name="dateFrom"
                               class="form-control" value="${dateFromSafe}">
                    </div>

                    <div class="col-md-2">
                        <label for="dateTo" class="form-label">Дата по</label>
                        <input type="date" id="dateTo" name="dateTo"
                               class="form-control" value="${dateToSafe}">
                    </div>

                    <div class="col-md-2">
                        <label for="amountFrom" class="form-label">Сумма от</label>
                        <input type="number" id="amountFrom" name="amountFrom"
                               class="form-control" value="${amountFromSafe}"
                               min="0" step="0.01">
                    </div>

                    <div class="col-md-2">
                        <label for="amountTo" class="form-label">Сумма до</label>
                        <input type="number" id="amountTo" name="amountTo"
                               class="form-control" value="${amountToSafe}"
                               min="0" step="0.01">
                    </div>

                    <div class="col-md-1 d-flex align-items-end gap-2">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-search"></i>
                        </button>
                        <a href="${Application.contextPath}/transactions" class="btn btn-outline-secondary w-100">
                            <i class="bi bi-x-circle"></i>
                        </a>
                    </div>
                </div>
                <div class="row g-3">
                    <div class="col-md-12">
                        <label for="tags" class="form-label">
                            <i class="bi bi-tags"></i> Теги
                        </label>
                        <div id="tagsContainer">
                            <#list tags as tag>
                                <span class="badge" style="background-color: ${tag.color};">
                                <input type="checkbox" name="tagIds" value="${tag.id?c}">
                                ${tag.name}
                                </span>
                            </#list>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Таблица транзакций -->
    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <#if transactions?has_content>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>Дата</th>
                            <th>Тип</th>
                            <th>Счет</th>
                            <th>Категория</th>
                            <th>Теги</th>
                            <th>Описание</th>
                            <th class="text-end">Сумма</th>
                            <th class="text-center" style="width: 120px;">Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list transactions as t>
                            <tr data-transaction-id="${t.id?c}">
                                <td>
                                     ${t.transactionDate}
                                </td>
                                <td>
                                    <#if t.type == 'INCOME'>
                                        <span class="badge bg-success">
                                            <i class="bi bi-arrow-up"></i> Доход
                                        </span>
                                    <#else>
                                        <span class="badge bg-danger">
                                            <i class="bi bi-arrow-down"></i> Расход
                                        </span>
                                    </#if>
                                </td>
                                <td>
                                    ${t.account.name}
                                </td>
                                <td>
                                    <#if categories?has_content>
                                        <#assign catName = "">
                                        <#list categories as cat>
                                            <#if cat.id == t.categoryId>
                                                <#assign catName = cat.name>
                                            </#if>
                                        </#list>
                                        <small>${catName!'-'}</small>
                                    <#else>
                                        <small>-</small>
                                    </#if>
                                </td>
                                <td>
                                    <#assign transactionTags = (tagsByTransaction[t.id?c])![]>
                                    <#if transactionTags?has_content>
                                        <div class="d-flex flex-wrap gap-1">
                                            <#list transactionTags as tag>
                                                <span class="badge" style="background-color: ${tag.color}; color: white;">${tag.name}</span>
                                            </#list>
                                        </div>
                                    <#else>
                                        <span class="text-muted small">-</span>
                                    </#if>
                                </td>
                                <td>
                                    <#if t.description??>
                                        ${t.description}
                                    <#else>
                                        <span class="text-muted">-</span>
                                    </#if>
                                </td>
                                <td class="text-end">
                                    <strong>
                                        <#if t.type == 'INCOME'>
                                            <span style="color: #10b981;">+${t.amount?string('0.00')} </span>
                                        <#else>
                                            <span style="color: #ef4444;">-${t.amount?string('0.00')}</span>
                                        </#if>
                                    </strong>
                                </td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-primary"
                                            onclick="editTransaction(${t.id?c}, '${t.accountId?c}', '${t.categoryId?c}', '${t.amount}', '${t.type}', '${t.transactionDate}', '${(t.description!'')}')"
                                            data-bs-toggle="modal"
                                            data-bs-target="#editModal"
                                            title="Редактировать">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <form method="post" action="${Application.contextPath}/transactions"
                                          style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${t.id?c}">
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
                        Транзакций не найдено.
                        <#if (currentTypeSafe != '') || (currentCategorySafe != '') || (dateFromSafe != '')>
                            Попробуйте изменить фильтры или
                        </#if>
                        <a href="#" data-bs-toggle="modal" data-bs-target="#createModal">создайте новую транзакцию</a>.
                    </p>
                </div>
            </#if>
        </div>
    </div>

    <!-- МОДАЛЬНОЕ ОКНО: СОЗДАНИЕ ТРАНЗАКЦИИ -->
    <div class="modal fade" id="createModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white border-0">
                    <h5 class="modal-title">
                        <i class="bi bi-plus-circle"></i> Добавить новую транзакцию
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>

                <form method="post" action="${Application.contextPath}/transactions" id="createForm">
                    <input type="hidden" name="action" value="create">

                    <div class="modal-body">
                        <!-- Выбор счета -->
                        <div class="mb-3">
                            <label for="createAccountId" class="form-label">
                                <i class="bi bi-wallet2"></i> Счет <span class="text-danger">*</span>
                            </label>
                            <select id="createAccountId" name="accountId" class="form-select" required>
                                <option value="">-- Выберите счет --</option>
                                <#if accounts?has_content>
                                    <#list accounts as acc>
                                        <option value="${acc.id?c}">
                                            ${acc.name} (${acc.currentBalance?string('0.00')} ${acc.currency.code})
                                        </option>
                                    </#list>
                                </#if>
                            </select>
                        </div>

                        <!-- Выбор типа транзакции -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="createType" class="form-label">
                                        <i class="bi bi-arrow-left-right"></i> Тип <span class="text-danger">*</span>
                                    </label>
                                    <select id="createType" name="type" class="form-select" required onchange="updateCategoryOptions('createCategoryId', this.value)">
                                        <option value="">-- Выберите тип --</option>
                                        <option value="INCOME">Доход</option>
                                        <option value="EXPENSE">Расход</option>
                                    </select>
                                </div>
                            </div>

                            <!-- Выбор категории -->
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="createCategoryId" class="form-label">
                                        <i class="bi bi-grid"></i> Категория <span class="text-danger">*</span>
                                    </label>
                                    <select id="createCategoryId" name="categoryId" class="form-select" required>
                                        <option value="">-- Выберите категорию --</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Сумма и дата -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="createAmount" class="form-label">
                                        <i class="bi bi-cash"></i> Сумма <span class="text-danger">*</span>
                                    </label>
                                    <input type="number" id="createAmount" name="amount" class="form-control"
                                           placeholder="0.00" min="0.01" step="0.01" required>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="createDate" class="form-label">
                                        <i class="bi bi-calendar-event"></i> Дата <span class="text-danger">*</span>
                                    </label>
                                    <input type="date" id="createDate" name="transactionDate" class="form-control"
                                           required>
                                </div>
                            </div>
                        </div>

                        <!-- Описание -->
                        <div class="mb-3">
                            <label for="createDescription" class="form-label">
                                <i class="bi bi-chat-dots"></i> Описание
                            </label>
                            <textarea id="createDescription" name="description" class="form-control"
                                      rows="2" placeholder="Добавьте описание для этой транзакции"></textarea>
                        </div>

                        <!-- Теги -->
                        <div class="mb-3">
                            <label for="createTags" class="form-label">
                                <i class="bi bi-tags"></i> Теги
                            </label>
                            <div id="createTags" class="d-flex flex-wrap gap-2">
                                <#if tags?has_content>
                                    <#list tags as tag>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" id="tag_${tag.id?c}"
                                                   name="tagIds" value="${tag.id?c}">
                                            <label class="form-check-label" for="tag_${tag.id?c}"
                                                   style="background-color: ${tag.color!'#6c757d'}; color: white; padding: 4px 8px; border-radius: 4px; cursor: pointer;">
                                                ${tag.name}
                                            </label>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer bg-light border-top">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="bi bi-x"></i> Отмена
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check2"></i> Добавить
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- МОДАЛЬНОЕ ОКНО: РЕДАКТИРОВАНИЕ ТРАНЗАКЦИИ -->
    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-warning text-dark border-0">
                    <h5 class="modal-title">
                        <i class="bi bi-pencil-square"></i> Редактировать транзакцию
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <form method="post" action="${Application.contextPath}/transactions" id="editForm">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" id="editId">

                    <div class="modal-body">
                        <!-- Выбор счета -->
                        <div class="mb-3">
                            <label for="editAccountId" class="form-label">
                                <i class="bi bi-wallet2"></i> Счет <span class="text-danger">*</span>
                            </label>
                            <select id="editAccountId" name="accountId" class="form-select" required>
                                <option value="">-- Выберите счет --</option>
                                <#if accounts?has_content>
                                    <#list accounts as acc>
                                        <option value="${acc.id?c}">
                                            ${acc.name} (${acc.currentBalance?string('0.00')} ${acc.currency.symbol})
                                        </option>
                                    </#list>
                                </#if>
                            </select>
                        </div>

                        <!-- Выбор типа и категории -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="editType" class="form-label">
                                        <i class="bi bi-arrow-left-right"></i> Тип <span class="text-danger">*</span>
                                    </label>
                                    <select id="editType" name="type" class="form-select" required
                                            onchange="updateCategoryOptions('editCategoryId', this.value)">
                                        <option value="">-- Выберите тип --</option>
                                        <option value="INCOME">Доход</option>
                                        <option value="EXPENSE">Расход</option>
                                    </select>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="editCategoryId" class="form-label">
                                        <i class="bi bi-grid"></i> Категория <span class="text-danger">*</span>
                                    </label>
                                    <select id="editCategoryId" name="categoryId" class="form-select" required>
                                        <option value="">-- Выберите категорию --</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Сумма и дата -->
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="editAmount" class="form-label">
                                        <i class="bi bi-cash"></i> Сумма <span class="text-danger">*</span>
                                    </label>
                                    <input type="number" id="editAmount" name="amount" class="form-control"
                                           placeholder="0.00" min="0.01" step="0.01" required>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="editDate" class="form-label">
                                        <i class="bi bi-calendar-event"></i> Дата <span class="text-danger">*</span>
                                    </label>
                                    <input type="date" id="editDate" name="transactionDate" class="form-control" required>
                                </div>
                            </div>
                        </div>

                        <!-- Описание -->
                        <div class="mb-3">
                            <label for="editDescription" class="form-label">
                                <i class="bi bi-chat-dots"></i> Описание
                            </label>
                            <textarea id="editDescription" name="description" class="form-control" rows="2"></textarea>
                        </div>

                        <!-- Теги -->
                        <div class="mb-3">
                            <label for="editTags" class="form-label">
                                <i class="bi bi-tags"></i> Теги
                            </label>
                            <div id="editTags" class="d-flex flex-wrap gap-2">
                                <#if tags?has_content>
                                    <#list tags as tag>
                                        <div class="form-check">
                                            <input class="form-check-input edit-tag-checkbox" type="checkbox"
                                                   id="editTag_${tag.id?c}" name="tagIds" value="${tag.id?c}">
                                            <label class="form-check-label" for="editTag_${tag.id?c}"
                                                   style="background-color: ${tag.color!'#6c757d'}; color: white; padding: 4px 8px; border-radius: 4px; cursor: pointer;">
                                                ${tag.name}
                                            </label>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
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

    <!-- Стили для таблицы и модалей -->
    <style>
        .table-hover tbody tr:hover {
            background-color: rgba(102, 126, 234, 0.05);
            transition: background-color 0.15s ease-in-out;
        }

        .form-label {
            font-weight: 500;
            color: #333;
        }

        .form-label .text-danger {
            margin-left: 2px;
        }

        .badge {
            font-weight: 500;
            padding: 6px 12px;
        }

        .modal-header {
            border-radius: 8px 8px 0 0;
        }

        .modal-body {
            max-height: 600px;
            overflow-y: auto;
        }

        .form-control:focus,
        .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }

        #createTags, #editTags {
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 4px;
            min-height: 50px;
        }

        .form-check-label {
            display: inline-block;
            margin-bottom: 0;
            transition: transform 0.15s ease;
        }

        .form-check-label:hover {
            transform: scale(1.05);
        }

        .form-check-input {
            margin-right: 6px;
            cursor: pointer;
        }
    </style>

    <!-- JavaScript для управления формами -->
    <script>
        // Данные категорий для быстрого доступа
        const categoriesData = {
            <#if categories?has_content>
            <#list categories as cat>
            ${cat.id?c}: {
                name: '${cat.name?js_string}',
                type: '${cat.type}'
            }<#if cat_has_next>,</#if>
            </#list>
            </#if>
        };


        function updateCategoryOptions(selectId, type) {
            const select = document.getElementById(selectId);
            select.innerHTML = '<option value="">-- Выберите категорию --</option>';

            for (const [id, cat] of Object.entries(categoriesData)) {
                if (cat.type === type) {
                    const option = document.createElement('option');
                    option.value = id;
                    option.textContent = cat.name;
                    select.appendChild(option);
                }
            }
        }


        function editTransaction(id, accountId, categoryId, amount, type, date, description) {
            // Заполняем ID
            document.getElementById('editId').value = id;

            // Заполняем счет
            document.getElementById('editAccountId').value = accountId;

            // Заполняем тип и обновляем категории
            document.getElementById('editType').value = type;
            updateCategoryOptions('editCategoryId', type);

            // Заполняем категорию (с задержкой)
            setTimeout(function() {
                document.getElementById('editCategoryId').value = categoryId;
            }, 50);

            // Заполняем остальные поля
            document.getElementById('editAmount').value = amount;
            document.getElementById('editDate').value = date;
            document.getElementById('editDescription').value = description || '';

            // Очищаем теги
            document.querySelectorAll('.edit-tag-checkbox').forEach(function(checkbox) {
                checkbox.checked = false;
            });
        }


        document.addEventListener('DOMContentLoaded', function() {
            // Устанавливаем сегодняшнюю дату как значение по умолчанию
            const today = new Date().toISOString().split('T')[0];
            const createDateInput = document.getElementById('createDate');
            if (createDateInput && !createDateInput.value) {
                createDateInput.value = today;
            }

            // Добавляем обработчик на форму создания
            const createForm = document.getElementById('createForm');
            if (createForm) {
                createForm.addEventListener('submit', function(e) {
                    const type = document.getElementById('createType').value;
                    const categoryId = document.getElementById('createCategoryId').value;

                    if (!type || !categoryId) {
                        e.preventDefault();
                        alert('Пожалуйста, заполните все обязательные поля');
                    }
                });
            }

            // Добавляем обработчик на форму редактирования
            const editForm = document.getElementById('editForm');
            if (editForm) {
                editForm.addEventListener('submit', function(e) {
                    const type = document.getElementById('editType').value;
                    const categoryId = document.getElementById('editCategoryId').value;

                    if (!type || !categoryId) {
                        e.preventDefault();
                        alert('Пожалуйста, заполните все обязательные поля');
                    }
                });
            }

            // Обработчик для кнопки "Добавить"
            const createModal = document.getElementById('createModal');
            if (createModal) {
                createModal.addEventListener('hidden.bs.modal', function() {
                    document.getElementById('createForm').reset();
                    document.getElementById('createType').value = '';
                    document.getElementById('createCategoryId').innerHTML = '<option value="">-- Выберите категорию --</option>';
                });
            }
        });
    </script>
</#macro>
