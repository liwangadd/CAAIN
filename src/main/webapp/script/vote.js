
let award_id = 0;

function awardBtnClick() {

}

$(function () {
    fetchAwards();

    function fetchAwards() {
        $.ajax({
            url: '/awards',
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: (result) => {
                let award_div = $("#awards_btn");
                award_id = result[0]['id'];
                fetchEntriesForType(award_id);
                result.forEach(function (award) {
                    award_div.append(`<button id="${award['id']}" class="btn btn-default" 
                        type="button" style="width:100px; margin-left: 8px" onclick="awardBtnClick()">
                        ${award['award_name']}</button>`)
                });

            }
        });
    }

    function fetchEntriesForType(award_id) {
        $.ajax({
            url: '/entries/' + award_id,
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: (result) => {
                console.log(result);
                let voteBody = $('#voteBody');
                voteBody.empty();
                for (let i = 0, len = result.length; i < len; ++i) {
                    voteBody.append(`<tr>
                        <td id="entryId">${i + 1}</td>
                        <td id="entry_id" hidden>${result[i]['id']}</td>
                        <td id="prize">
                            <select class="form-control">
                                <option>无</option>
                                <option>一等奖</option>
                                <option>二等奖</option>
                                <option>三等奖</option>
                            </select>
                        </td>
                        <td>${result[i]['expert_num']}</td>
                        <td id="entryName">${result[i]['entry_name']}</td>
                        <td></td>
                    </tr>`)
                }

            }
        })
    }

    //
    // function fetchData() {
    //     var voteTable = $('#voteBody');
    //     entries = ['李金屏-基于巡检机器人的特殊地域设备故障和实时报警的智能视频监测系统',
    //         "苏中-高频响低噪声大过载新型振动陀螺技术及应用",
    //         "自然科学1", "自然科学2"]
    //     for (var i = 0; i < entries.length; i++) {
    //         voteTable.append(`<tr>
    //             <td id="entryId">${i + 1}</td>
    //             <td id="prize">
    //                 <select class="selectpicker">
    //                     <option>无</option>
    //                     <option>一等奖</option>
    //                     <option>二等奖</option>
    //                     <option>三等奖</option>
    //                 </select>
    //             </td>
    //             <td>11</td>
    //             <td id="entryName">${entries[i]}</td>
    //             <td></td>
    //         </tr>`)
    //     }
    // }
    //
    // function submitVote() {
    //     var result = [];
    //     $('#voteBody').find('tr').each(function () {
    //         var id = $(this).find('#entryId').text();
    //         var entryName = $(this).find("#entryName").text();
    //         var prize = $(this).find('option:selected').text();
    //         result.push({ 'id': id, 'entryName': entryName, 'prize': prize });
    //     })
    //     console.log(result);
    // }
    //
    // $('#submitVote').click(function () {
    //     Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
    //         if (!e) {
    //             return;
    //         }
    //         submitVote();
    //         // $.ajax({
    //         //     type: "post",
    //         //     url: "/api/DepartmentApi/Delete",
    //         //     data: { "": JSON.stringify(arrselections) },
    //         //     success: function (data, status) {
    //         //         if (status == "success") {
    //         //             toastr.success('提交数据成功');
    //         //             $("#tb_departments").bootstrapTable('refresh');
    //         //         }
    //         //     },
    //         //     error: function () {
    //         //         toastr.error('Error');
    //         //     },
    //         //     complete: function () {
    //
    //         //     }
    //
    //         // });
    //     });
    // });
    //
    // $("#showResult").click(function () {
    //     var voteThead = $('#voteThead');
    //     voteThead.empty()
    //     voteThead.append(`<tr>
    //             <th class="text-center">序号</th>
    //             <th class="text-center">总专家数</th>
    //             <th class="text-center">文档名称</th>
    //             <th class="text-center">获奖结果</th>
    //         </tr>`)
    //     var voteBody = $("#voteBody");
    //     voteBody.empty();
    //     entries = ['李金屏-基于巡检机器人的特殊地域设备故障和实时报警的智能视频监测系统',
    //         "苏中-高频响低噪声大过载新型振动陀螺技术及应用",
    //         "自然科学1", "自然科学2"]
    //     for (var i = 0; i < entries.length; i++) {
    //         voteBody.append(`<tr>
    //             <td id="entryId">${i + 1}</td>
    //             <td>11</td>
    //             <td id="entryName">${entries[i]}</td>
    //             <td>一等奖</td>
    //         </tr>`)
    //     }
    // })
})