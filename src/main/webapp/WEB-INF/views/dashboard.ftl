<#include "base.ftl">

<#macro title>Панель управления - Финансовый Трекер</#macro>

<#macro content>
    <h1 class="mb-4">
        <i class="bi bi-house-door"></i>
        Добро пожаловать, ${Session.user.firstName!Session.user.username}!
    </h1>

    <!-- ОБЩИЙ БАЛАНС И СЧЕТА -->
    <div class="row g-3 mb-4">
        <!-- Общий баланс -->
        <div class="col-lg-3">
            <div class="card border-0 shadow-sm stat-card-primary" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h6 class="mb-0" style="opacity: 0.9;">Общий баланс</h6>
                        <i class="bi bi-wallet2" style="font-size: 1.5rem; opacity: 0.7;"></i>
                    </div>
                    <h2 class="mb-0">
                        <#if totalBalance?? && totalBalance gt 0>
                            +${totalBalance?string('0.00')} ₽
                        <#else>
                            ${totalBalance?string('0.00')} ₽
                        </#if>
                    </h2>
                    <p class="mb-0 mt-2 small" style="opacity: 0.9;">Все счета объединены</p>
                </div>
            </div>
        </div>

        <!-- Счета -->
        <#if accounts?has_content>
            <#list accounts as account>
                <#if account_index < 3>
                    <div class="col-lg-3">
                        <div class="card border-0 shadow-sm stat-card-account">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <h6 class="text-muted mb-0">${account.name}</h6>
                                    <span class="badge bg-info text-dark">${account.currency.code}</span>
                                </div>
                                <h3 class="mb-2">
                                    <#if account.currentBalance gt 0>
                                        <span style="color: #10b981;">${account.currentBalance?string('0.00')}${account.currency.symbol}</span>
                                    <#else>
                                        <span style="color: #ef4444;">${account.currentBalance?string('0.00')}${account.currency.symbol}</span>
                                    </#if>
                                </h3>
                                <small class="text-muted d-block">Текущий баланс</small>
                                <a href="${Application.contextPath}/accounts" class="btn btn-sm btn-link p-0 mt-2">
                                    Управлять →
                                </a>
                            </div>
                        </div>
                    </div>
                </#if>
            </#list>
        </#if>
    </div>

    <!-- РАСШИРЕННЫЙ СПИСОК ВСЕХ СЧЕТОВ (если их больше 3) -->
    <#if accounts?has_content && (accounts?size > 3)>
        <div class="card shadow-sm mb-4 border-0">
            <div class="card-header bg-white border-bottom">
                <h5 class="mb-0">
                    <i class="bi bi-list-check"></i> Все счета
                </h5>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>Название</th>
                            <th>Валюта</th>
                            <th class="text-end">Начальный баланс</th>
                            <th class="text-end">Текущий баланс</th>
                            <th class="text-center">Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list accounts as account>
                            <tr>
                                <td>
                                    <i class="bi bi-wallet2" style="color: #667eea;"></i>
                                    <strong>${account.name}</strong>
                                </td>
                                <td>
                                    <span class="badge bg-light text-dark">${account.currency}</span>
                                </td>
                                <td class="text-end">
                                    ${account.initialBalance?string('0.00')} ₽
                                </td>
                                <td class="text-end">
                                    <#if account.currentBalance gt 0>
                                        <span style="color: #10b981; font-weight: bold;">
                                            +${account.currentBalance?string('0.00')}
                                        </span>
                                    <#else>
                                        <span style="color: #ef4444; font-weight: bold;">
                                            ${account.currentBalance?string('0.00')}
                                        </span>
                                    </#if>
                                </td>
                                <td class="text-center">
                                    <a href="${Application.contextPath}/accounts?id=${account.id}"
                                       class="btn btn-sm btn-outline-primary" title="Просмотреть">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </#if>

    <!-- Статистика за периоды -->
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted mb-3">
                        <i class="bi bi-calendar-day"></i> Сегодня
                    </h6>
                    <div class="income mb-2">
                        <small class="text-muted">Доход</small>
                        <div class="text-success fw-bold">${todayIncome?string('0.00')} ₽</div>
                    </div>
                    <div class="expense mb-2">
                        <small class="text-muted">Расход</small>
                        <div class="text-danger fw-bold">${todayExpense?string('0.00')} ₽</div>
                    </div>
                    <hr class="my-2">
                    <small class="text-muted">Итого:</small>
                    <div class="fw-bold fs-5">
                        <#if todayBalance gte 0>
                            <span style="color: #10b981;">+${todayBalance?string('0.00')}</span>
                        <#else>
                            <span style="color: #ef4444;">${todayBalance?string('0.00')}</span>
                        </#if>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted mb-3">
                        <i class="bi bi-calendar-week"></i> За неделю
                    </h6>
                    <div class="income mb-2">
                        <small class="text-muted">Доход</small>
                        <div class="text-success fw-bold">${weekIncome?string('0.00')} ₽</div>
                    </div>
                    <div class="expense mb-2">
                        <small class="text-muted">Расход</small>
                        <div class="text-danger fw-bold">${weekExpense?string('0.00')} ₽</div>
                    </div>
                    <hr class="my-2">
                    <small class="text-muted">Итого:</small>
                    <div class="fw-bold fs-5">
                        <#if weekBalance gte 0>
                            <span style="color: #10b981;">+${weekBalance?string('0.00')}</span>
                        <#else>
                            <span style="color: #ef4444;">${weekBalance?string('0.00')}</span>
                        </#if>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted mb-3">
                        <i class="bi bi-calendar"></i> За месяц
                    </h6>
                    <div class="income mb-2">
                        <small class="text-muted">Доход</small>
                        <div class="text-success fw-bold">${monthIncome?string('0.00')} ₽</div>
                    </div>
                    <div class="expense mb-2">
                        <small class="text-muted">Расход</small>
                        <div class="text-danger fw-bold">${monthExpense?string('0.00')} ₽</div>
                    </div>
                    <hr class="my-2">
                    <small class="text-muted">Итого:</small>
                    <div class="fw-bold fs-5">
                        <#if monthBalance gte 0>
                            <span style="color: #10b981;">+${monthBalance?string('0.00')}</span>
                        <#else>
                            <span style="color: #ef4444;">${monthBalance?string('0.00')}</span>
                        </#if>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card stat-card border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted mb-3">
                        <i class="bi bi-calendar2"></i> За год
                    </h6>
                    <div class="income mb-2">
                        <small class="text-muted">Доход</small>
                        <div class="text-success fw-bold">${yearIncome?string('0.00')} ₽</div>
                    </div>
                    <div class="expense mb-2">
                        <small class="text-muted">Расход</small>
                        <div class="text-danger fw-bold">${yearExpense?string('0.00')} ₽</div>
                    </div>
                    <hr class="my-2">
                    <small class="text-muted">Итого:</small>
                    <div class="fw-bold fs-5">
                        <#if yearBalance gte 0>
                            <span style="color: #10b981;">+${yearBalance?string('0.00')}</span>
                        <#else>
                            <span style="color: #ef4444;">${yearBalance?string('0.00')}</span>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Графики -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="bi bi-pie-chart"></i> Расходы по категориям (месяц)
                    </h5>
                    <canvas id="expensesChart" style="max-height: 300px;"></canvas>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="bi bi-pie-chart-fill"></i> Доходы по категориям (месяц)
                    </h5>
                    <canvas id="incomeChart" style="max-height: 300px;"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Последние транзакции -->
    <div class="card shadow-sm border-0">
        <div class="card-header bg-white border-bottom">
            <h5 class="mb-0">
                <i class="bi bi-clock-history"></i> Последние транзакции
            </h5>
        </div>
        <div class="card-body p-0">
            <#if recentTransactions?has_content>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>Дата</th>
                            <th>Тип</th>
                            <th>Счет</th>
                            <th>Описание</th>
                            <th>Категория</th>
                            <th class="text-end">Сумма</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list recentTransactions as t>
                            <tr>
                                <td>${t.transactionDate}</td>
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
                                <td>${t.account.name!'-'}</td>
                                <td>${t.description!'-'}</td>
                                <td>${t.category.name!'-'}</td>
                                <td class="text-end">
                                    <strong>
                                        <#if t.type == 'INCOME'>
                                            <span style="color: #10b981;">+${t.amount?string('0.00')} ${t.account.currency.symbol}</span>
                                        <#else>
                                            <span style="color: #ef4444;">-${t.amount?string('0.00')} ${t.account.currency.symbol}</span>
                                        </#if>
                                    </strong>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="card-footer bg-white text-center border-top">
                    <a href="${Application.contextPath}/transactions" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-list"></i> Все транзакции
                    </a>
                </div>
            <#else>
                <div class="text-center py-5">
                    <i class="bi bi-inbox text-muted" style="font-size: 3rem;"></i>
                    <p class="text-muted mt-3">
                        Транзакций пока нет.
                        <a href="${Application.contextPath}/transactions">Создайте первую!</a>
                    </p>
                </div>
            </#if>
        </div>
    </div>

    <!-- Стили для карточек -->
    <style>
        .stat-card {
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1) !important;
        }

        .stat-card-primary {
            min-height: 150px;
            display: flex;
            align-items: center;
        }

        .stat-card-account {
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .stat-card-account:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15) !important;
        }

        .income {
            color: #10b981;
        }

        .expense {
            color: #ef4444;
        }

        .btn-link {
            font-weight: 500;
            text-decoration: none;
        }

        .btn-link:hover {
            text-decoration: underline;
        }

        .table-hover tbody tr:hover {
            background-color: rgba(102, 126, 234, 0.05);
        }
    </style>

    <!-- Chart.js скрипты -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <script>
        const expensesData = ${expensesChartData};
        const incomeData = ${incomeChartData};

        // Функция для генерации цветов
        function getColors(count) {
            const colors = [
                '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0',
                '#9966FF', '#FF9F40', '#E7E9ED', '#C9CBCF',
                '#667eea', '#764ba2', '#f093fb', '#4facfe'
            ];
            return colors.slice(0, count);
        }

        // Расходы Pie Chart
        if (Object.keys(expensesData).length > 0) {
            new Chart(document.getElementById('expensesChart'), {
                type: 'doughnut',
                data: {
                    labels: Object.keys(expensesData),
                    datasets: [{
                        data: Object.values(expensesData),
                        backgroundColor: getColors(Object.keys(expensesData).length),
                        borderColor: '#fff',
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                padding: 15,
                                font: { size: 12 }
                            }
                        }
                    },
                    onClick: () => {
                        window.location.href = '${Application.contextPath}/transactions?type=EXPENSE';
                    }
                }
            });
        } else {
            document.getElementById('expensesChart').parentElement.innerHTML =
                '<p class="text-muted text-center py-5">Нет данных о расходах</p>';
        }

        // Доходы Pie Chart
        if (Object.keys(incomeData).length > 0) {
            new Chart(document.getElementById('incomeChart'), {
                type: 'doughnut',
                data: {
                    labels: Object.keys(incomeData),
                    datasets: [{
                        data: Object.values(incomeData),
                        backgroundColor: getColors(Object.keys(incomeData).length),
                        borderColor: '#fff',
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                padding: 15,
                                font: { size: 12 }
                            }
                        }
                    },
                    onClick: () => {
                        window.location.href = '${Application.contextPath}/transactions?type=INCOME';
                    }
                }
            });
        } else {
            document.getElementById('incomeChart').parentElement.innerHTML =
                '<p class="text-muted text-center py-5">Нет данных о доходах</p>';
        }
    </script>
</#macro>