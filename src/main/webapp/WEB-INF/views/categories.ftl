<#include "base.ftl">

<#macro title>Категории - Финансовый Трекер</#macro>

<#macro content>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1><i class="bi bi-grid"></i> Категории</h1>
        <button class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#createModal">
            <i class="bi bi-plus-circle"></i> Добавить категорию
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

    <!-- Разделение по типам -->
    <div class="row">
        <!-- Доходы -->
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0"><i class="bi bi-arrow-up-circle"></i> Доходы</h5>
                </div>
                <div class="card-body p-0">
                    <#assign incomeCategories = categories?filter(c -> c.type == 'INCOME')>
                    <#if incomeCategories?has_content>
                        <ul class="list-group list-group-flush">
                            <#list incomeCategories as cat>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <div class="d-flex align-items-center">
                                        <span class="badge me-2"
                                              style="background-color: ${cat.color!'#6c757d'}; font-size: 1.2rem;">
                                            <i class="bi bi-${cat.icon!'star'}"></i>
                                        </span>
                                        <span>${cat.name}</span>
                                    </div>
                                    <div>
                                        <button class="btn btn-sm btn-outline-primary"
                                                onclick="editCategory(${cat.id}, '${cat.name}', '${cat.type}', '${cat.color!'#6c757d'}', '${cat.icon!'star'}')">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form method="post" action="${Application.contextPath}/categories"
                                              style="display:inline;" onsubmit="return confirm('Удалить категорию?')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${cat.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </li>
                            </#list>
                        </ul>
                    <#else>
                        <div class="text-center py-4 text-muted">
                            Нет категорий доходов
                        </div>
                    </#if>
                </div>
            </div>
        </div>

        <!-- Расходы -->
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-header bg-danger text-white">
                    <h5 class="mb-0"><i class="bi bi-arrow-down-circle"></i> Расходы</h5>
                </div>
                <div class="card-body p-0">
                    <#assign expenseCategories = categories?filter(c -> c.type == 'EXPENSE')>
                    <#if expenseCategories?has_content>
                        <ul class="list-group list-group-flush">
                            <#list expenseCategories as cat>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <div class="d-flex align-items-center">
                                        <span class="badge me-2"
                                              style="background-color: ${cat.color!'#6c757d'}; font-size: 1.2rem;">
                                            <i class="bi bi-${cat.icon!'star'}"></i>
                                        </span>
                                        <span>${cat.name}</span>
                                    </div>
                                    <div>
                                        <button class="btn btn-sm btn-outline-primary"
                                                onclick="editCategory(${cat.id}, '${cat.name}', '${cat.type}', '${cat.color!'#6c757d'}', '${cat.icon!'star'}')">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form method="post" action="${Application.contextPath}/categories"
                                              style="display:inline;" onsubmit="return confirm('Удалить категорию?')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${cat.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </li>
                            </#list>
                        </ul>
                    <#else>
                        <div class="text-center py-4 text-muted">
                            Нет категорий расходов
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <!-- Модальное окно создания -->
    <div class="modal fade" id="createModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" action="${Application.contextPath}/categories">
                    <div class="modal-header">
                        <h5 class="modal-title">Создать категорию</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create">

                        <div class="mb-3">
                            <label for="name" class="form-label">Название</label>
                            <input type="text" class="form-control" id="name" name="name"
                                   required placeholder="Например: Продукты">
                        </div>

                        <div class="mb-3">
                            <label for="type" class="form-label">Тип</label>
                            <select class="form-select" id="type" name="type" required>
                                <option value="INCOME">Доход</option>
                                <option value="EXPENSE" selected>Расход</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="color" class="form-label">Цвет</label>
                            <div class="d-flex align-items-center gap-2">
                                <input type="color" class="form-control form-control-color"
                                       id="color" name="color" value="#6c757d">
                                <span class="text-muted">Выберите цвет для категории</span>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="icon" class="form-label">Иконка (Bootstrap Icons)</label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="bi bi-star" id="iconPreview"></i>
                                </span>
                                <input type="text" class="form-control" id="icon" name="icon"
                                       value="star" placeholder="star"
                                       oninput="updateIconPreview('iconPreview', this.value)">
                            </div>
                            <small class="text-muted">
                                Примеры: cart, house, car, heart, briefcase, gift, phone, camera
                                <br>
                                <a href="https://icons.getbootstrap.com/" target="_blank">
                                    Все иконки Bootstrap
                                </a>
                            </small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-primary">Создать</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Модальное окно редактирования -->
    <div class="modal fade" id="editModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" action="${Application.contextPath}/categories">
                    <div class="modal-header">
                        <h5 class="modal-title">Редактировать категорию</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" id="editId">

                        <div class="mb-3">
                            <label for="editName" class="form-label">Название</label>
                            <input type="text" class="form-control" id="editName" name="name" required>
                        </div>

                        <div class="mb-3">
                            <label for="editType" class="form-label">Тип</label>
                            <select class="form-select" id="editType" name="type" required>
                                <option value="INCOME">Доход</option>
                                <option value="EXPENSE">Расход</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="editColor" class="form-label">Цвет</label>
                            <div class="d-flex align-items-center gap-2">
                                <input type="color" class="form-control form-control-color"
                                       id="editColor" name="color">
                                <span class="text-muted">Выберите цвет для категории</span>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="editIcon" class="form-label">Иконка (Bootstrap Icons)</label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="bi bi-star" id="editIconPreview"></i>
                                </span>
                                <input type="text" class="form-control" id="editIcon" name="icon"
                                       placeholder="star"
                                       oninput="updateIconPreview('editIconPreview', this.value)">
                            </div>
                            <small class="text-muted">
                                Примеры: cart, house, car, heart, briefcase, gift, phone, camera
                                <br>
                                <a href="https://icons.getbootstrap.com/" target="_blank">
                                    Все иконки Bootstrap
                                </a>
                            </small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function editCategory(id, name, type, color, icon) {
            document.getElementById('editId').value = id;
            document.getElementById('editName').value = name;
            document.getElementById('editType').value = type;
            document.getElementById('editColor').value = color;
            document.getElementById('editIcon').value = icon;

            // Обновляем превью иконки
            updateIconPreview('editIconPreview', icon);

            const editModal = new bootstrap.Modal(document.getElementById('editModal'));
            editModal.show();
        }

        function updateIconPreview(previewId, iconName) {
            const preview = document.getElementById(previewId);
            if (preview) {
                preview.className = 'bi bi-' + (iconName || 'star');
            }
        }
    </script>
</#macro>