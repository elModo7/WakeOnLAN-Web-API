function onInitComplete() {
    var api = this.api();
    var id = api.table().node().id    
    var config = api.settings().init();

    //if (config.serverSide) {

        var filterRow = $('<tr>').appendTo('#' + id + '_wrapper thead:first');
        $('#' + id).parent().css('height', 'auto');
        //filterRow.appendTo('#' + id + ' thead');

        api.columns().eq(0).each(function (colIndex) {
            var column = api.column(colIndex)
            if (column.visible()) {
                var filterCell = $('<th class="p-1">').appendTo(filterRow);// filterRow.find("th:eq(" + colIndex + ")");
                var searchable = config.columns[colIndex].searchable;
                if (typeof searchable == "undefined") {
                    searchable = true;
                }
                if (searchable) {

                    var filter = config.columns[colIndex].filter;
                    if (typeof filter == "undefined") {
                        filter = {};
                    }

                    var CellAriaLabelValue = "Buscar por " + $("#" + id + " thead tr:eq(0) th:eq(" + colIndex + ")").text();
                    var filterName = filter.name || config.columns[colIndex].data;
                    var filterType = filter.type || "text";
                    switch (filterType) {
                        case "text":
                            $('<input type="text" aria-label="' + CellAriaLabelValue + '" data-filter="' + colIndex + '" name="' + filterName + '" class="form-control form-control-sm" />')
                            .appendTo(filterCell)
                            .on('keyup change clear', function () {
                                if (column.search() !== this.value) {
                                    column.search(this.value).draw();
                                }
                            });
                            break;
                        case "number":
                            $('<input type="number" aria-label="' + CellAriaLabelValue + '" data-filter="' + colIndex + '" name="' + filterName + '" class="form-control form-control-sm" />')
                            .appendTo(filterCell)
                            .on('keyup mouseup', function () {
                                if (column.search() !== this.value) {
                                    column.search(this.value).draw();
                                }
                            });
                            break;
                        case "select":
                            if (typeof filter.clone == "string") {
                                var select = $(filter.clone).clone();
                                select.show();
                                select.attr("data-filter", colIndex);
                                select.attr("aria-label", CellAriaLabelValue);
                                select.attr("name", filterName);
                                select.removeAttr("id");
                                select.on('change', function () {
                                     var val = $.fn.dataTable.util.escapeRegex($(this).val());
                                     var i = parseInt($(this).attr("data-filter"));
                                     var table = api.table();
                                     $.fn.dataTable.ext.search.pop();
                                     table.draw();
                                     if (val != '') {
                                        $.fn.dataTable.ext.search.push(
                                            function(settings, data, dataIndex) {
                                                var active = val == 'true' ? 'active' : 'inactive';
                                                return (table.row(dataIndex).data()[config.columns[i].data]).toString() == val;
                                            }
                                        );
                                        table.draw();
                                     }
                                   }).appendTo(filterCell);
                            }
                            else if (typeof filter.options != "undefined") {
                                var select = $('<select aria-label="' + CellAriaLabelValue + '" data-filter="' + colIndex + '" name="' + filterName + '" class="form-control form-control-sm" />');
                                for (var i = 0; i < filter.options.length; i++) {
                                    select.append('<option value="' + (filter.options[i].value || "") + '">' + (filter.options[i].text || "") + '</option>');
                                }
                                select.on('change', function () {
                                    var val = $.fn.dataTable.util.escapeRegex($(this).val());
                                    var i = parseInt($(this).attr("data-filter"));
                                    var table = api.table();
                                    $.fn.dataTable.ext.search.pop();
                                    table.draw();
                                    if (val != '') {
                                       $.fn.dataTable.ext.search.push(
                                           function(settings, data, dataIndex) {
                                               return (table.row(dataIndex).data()[config.columns[i].data]).toString() == val;
                                           }
                                       );
                                       table.draw();
                                    }
                                }).appendTo(filterCell);
                            }
                            else {
                                filterCell.html('');
                            }
                            break;
                        case "datepicker":
                            var input = $('<input type="text" aria-label="' + CellAriaLabelValue + '" data-filter="' + colIndex + '" name="' + filterName + '" class="form-control form-control-sm" />');
                            input.appendTo(filterCell)
                            .on('change', function () {
                                if (column.search() !== this.value) {
                                    column.search(this.value).draw();
                                }
                            });
                            //filterCell.html('<input type="text" aria-label="' + CellAriaLabelValue + '" data-filter="' + colIndex + '" name="' + filterName + '" class="form-control form-control-sm" />');
                            //$('input[data-filter="' + colIndex + '"]').datepicker();
                            break;
                    }
                }
            }

        });
        api.table().columns.adjust().draw();
        //Recargar la tabla al poner un filtro
        /*
        $('#' + id + ' [data-filter]').on('keyup change', function () {
            //api.ajax.reload(function () { });
            console.log("change", api.columns());
            api.columns().every(function () {
                var column = this;
                var value = $("#" + id + " [data-filter='" + column[0][0] + "']").val();
                console.log(column[0], column.search(), value);
                if (column.search() !== value) {

                    column.search(value).draw();
                }
            });
            //api.table().draw();
        });
        */
    //}
}