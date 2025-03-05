package org.isite.operation.service;

import org.apache.commons.lang3.BooleanUtils;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.sync.ConcurrentError;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.converter.PrizeRecordConverter;
import org.isite.operation.mapper.PrizeRecordMapper;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.prize.PrizeGiver;
import org.isite.operation.prize.PrizeGiverFactory;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.task.IdempotentKey;
import org.isite.user.data.constants.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @Description 奖品类型活动 Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeRecordService extends TaskRecordService<PrizeRecordPo> {
    private PrizeService prizeService;
    private PrizeGiverFactory prizeGiverFactory;

    @Autowired
    public PrizeRecordService(PrizeRecordMapper prizeRecordMapper) {
        super(prizeRecordMapper);
    }

    @Autowired
    public void setPrizeGiverFactory(PrizeGiverFactory prizeGiverFactory) {
        this.prizeGiverFactory = prizeGiverFactory;
    }

    /**
     * 统计领奖记录。receiveStatus可以为空
     */
    public int count(int activityId, long userId, Boolean receiveStatus) {
        PrizeRecordPo recordPo = new PrizeRecordPo();
        recordPo.setUserId(userId);
        recordPo.setActivityId(activityId);
        recordPo.setReceiveStatus(receiveStatus);
        return this.count(recordPo);
    }

    /**
     * 查询用户已领取的领奖记录。如果是主活动，任务记录包含了主活动及其子活动（两级）的领奖记录
     */
    public List<PrizeRecordPo> findReceived(int activityId, long userId) {
        Weekend<PrizeRecordPo> weekend = Weekend.of(PrizeRecordPo.class);
        Example example = new Example(getPoClass());
        weekend.and(example.createCriteria().andEqualTo(UserConstants.FIELD_USER_ID, userId)
                .andEqualTo(OperationConstants.FIELD_RECEIVE_STATUS, Boolean.TRUE));
        weekend.and(example.createCriteria().orEqualTo(OperationConstants.FIELD_ACTIVITY_ID, activityId)
                .orEqualTo(OperationConstants.FIELD_ACTIVITY_PID, activityId));
        return super.findList(weekend);
    }

    /**
     * 获取用户未领取的领奖记录
     * @param prizeId 可以为空
     */
    public PrizeRecordPo getNotReceive(int activityId, Integer prizeId, long userId) {
        return ((PrizeRecordMapper) getMapper()).selectOneNotReceive(activityId, prizeId, userId);
    }

    /**
     * 将第三方奖品值更新到奖品记录中
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateThirdPrizeValue(Long prizeRecordId, String thirdPrizeValue) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        prizeRecordPo.setId(prizeRecordId);
        prizeRecordPo.setThirdPrizeValue(thirdPrizeValue);
        updateSelectiveById(prizeRecordPo);
    }

    /**
     * 更新奖品记录为已领取
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateReceiveStatus(PrizeRecordPo prizeRecordPo, Prize prize) {
        prizeRecordPo.setReceiveStatus(Boolean.TRUE);
        prizeRecordPo.setReceiveTime(LocalDateTime.now());
        PrizeRecordConverter.toPrizeRecordPo(prizeRecordPo, prize);
        Weekend<PrizeRecordPo> weekend = Weekend.of(PrizeRecordPo.class);
        weekend.weekendCriteria().andEqualTo(PrizeRecordPo::getId, prizeRecordPo.getPrizeId())
                // 使用乐观锁控制并发场景
                .andEqualTo(PrizeRecordPo::getReceiveStatus, Boolean.FALSE);
        int rows = getMapper().updateByExampleSelective(prizeRecordPo, weekend);
        Assert.isTrue(rows > Constants.ZERO, new ConcurrentError());
    }

    /**
     * 更新奖品信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int updatePrize(Long recordId, Prize prize) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        prizeRecordPo.setId(recordId);
        PrizeRecordConverter.toPrizeRecordPo(prizeRecordPo, prize);
        return updateSelectiveById(prizeRecordPo);
    }

    /**
     * 统计在当前任务周期内完成的奖品记录个数
     */
    public int countPrizeRecord(Integer activityId, Integer taskId, @Nullable LocalDateTime startTime, Long userId) {
        Weekend<PrizeRecordPo> weekend = Weekend.of(PrizeRecordPo.class);
        WeekendCriteria<PrizeRecordPo, Object> criteria = weekend.weekendCriteria()
                .andEqualTo(PrizeRecordPo::getUserId, userId)
                .andEqualTo(PrizeRecordPo::getActivityId, activityId)
                .andEqualTo(PrizeRecordPo::getTaskId, taskId);
        if (null != startTime) {
            criteria.andGreaterThanOrEqualTo(PrizeRecordPo::getFinishTime, startTime);
        }
        return this.count(weekend);
    }

    /**
     * 领取奖品（注意：该方法会校验奖品的库存）
     */
    @Transactional(rollbackFor = Exception.class)
    public int receivePrize(Activity activity, Prize prize, long userId) {
        List<PrizeRecordPo> prizeRecordPos = findList(PrizeRecordConverter.toPrizeRecordSelectivePo(prize.getId(), userId, Boolean.FALSE));
        Assert.notEmpty(prizeRecordPos, MessageUtils.getMessage("prize.notFound", "prize not found"));
        PrizeGiver prizeGiver = prizeGiverFactory.get(prize.getPrizeType());
        prizeRecordPos.forEach(prizeRecordPo -> prizeGiver.execute(activity, prize, prizeRecordPo));
        return prizeRecordPos.size();
    }

    /**
     * 管理员给用户赠送领奖记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int addPrizeRecord(
            ActivityPo activityPo, int prizeId, long userId, long employeeId) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        if (Constants.ZERO != prizeId) {
            PrizePo prizePo = prizeService.get(prizeId);
            Assert.notNull(prizePo, MessageUtils.getMessage("prize.notFound", "prize not found"));
            Assert.isTrue(prizePo.getActivityId().equals(activityPo.getId()), new IllegalParameterError());
            PrizeRecordConverter.toPrizeRecordPo(prizeRecordPo, prizePo);
            prizeRecordPo.setLockStatus(Boolean.TRUE);
            prizeService.incrLockInventory(prizePo);
        } else {
            prizeRecordPo.setLockStatus(Boolean.FALSE);
        }
        prizeRecordPo.setObjectType(ObjectType.TENANT_EMPLOYEE);
        prizeRecordPo.setObjectValue(String.valueOf(employeeId));
        prizeRecordPo.setReceiveStatus(Boolean.FALSE);
        prizeRecordPo.setUserId(userId);
        prizeRecordPo.setActivityId(activityPo.getId());
        prizeRecordPo.setActivityPid(activityPo.getPid());
        prizeRecordPo.setTaskId(Constants.ZERO);
        prizeRecordPo.setFinishTime(LocalDateTime.now());
        prizeRecordPo.setIdempotentKey(IdempotentKey.toValue(
                activityPo.getId(), Constants.ZERO, null, userId,
                count(PrizeRecordConverter.toPrizeRecordSelectivePo(activityPo.getId(), Constants.ZERO, userId))));
        return this.insert(prizeRecordPo);
    }

    /**
     * 只能删除赠送的未领取的领奖记录，如果已锁定奖品需要回滚锁定库存和已消耗库存
     */
    @Transactional(rollbackFor = Exception.class)
    public int deletePrizeRecord(PrizeRecordPo prizeRecordPo) {
        if (BooleanUtils.isTrue(prizeRecordPo.getLockStatus())) {
            prizeService.rollbackLockInventory(prizeRecordPo.getPrizeId());
        }
        return this.delete(prizeRecordPo.getId());
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }
}
