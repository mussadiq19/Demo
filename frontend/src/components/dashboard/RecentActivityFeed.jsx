import { useRecentRisks } from '../../hooks/useRecentRisks';
import RiskAlertRow from './RiskAlertRow';
import SkeletonList from '../common/SkeletonList';
import EmptyState from '../common/EmptyState';

export default function RecentActivityFeed() {
  const { data, isLoading } = useRecentRisks();
  const risks = data?.content || [];

  if (isLoading) {
    return <SkeletonList />;
  }

  if (!risks.length) {
    return (
      <EmptyState
        title="All clear"
        message="No active alerts — all clear."
      />
    );
  }

  return (
    <div>
      {risks.map((risk) => (
        <RiskAlertRow key={risk.id} risk={risk} />
      ))}
    </div>
  );
}
