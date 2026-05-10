import React from 'react';
import { useState } from 'react';
import { useDashboard } from '../../hooks/useDashboard';
import { useSkillsGap } from '../../hooks/useSkillsGap';
import { useAuth } from '../../hooks/useAuth';
import { riskService } from '../../services/riskService';
import MetricCard from '../dashboard/MetricCard';
import DepartmentGapRow from '../dashboard/DepartmentGapRow';
import ForesightInsightBox from '../dashboard/ForesightInsightBox';
import RecentActivityFeed from '../dashboard/RecentActivityFeed.jsx';
import DashboardSkeleton from '../common/DashboardSkeleton';

export default function DashboardOverview() {
  const { user } = useAuth();
  const { data: dashboard, isLoading: dashLoading } = useDashboard();
  const { data: gapsData,   isLoading: gapsLoading  } = useSkillsGap();
  const [isScanning, setIsScanning] = useState(false);

  if (dashLoading) return <DashboardSkeleton />;

  const handleScan = async () => {
    if (!user?.companyId || isScanning) return;
    setIsScanning(true);
    try {
      await riskService.triggerScan(user.companyId);
    } finally {
      setIsScanning(false);
    }
  };

  const lastScanLabel = dashboard?.lastScanAt
    ? new Date(dashboard.lastScanAt).toLocaleString()
    : 'Never';

  return (
    <div>
      <div className="flex items-center justify-between bg-white border border-surface-border rounded-2xl px-6 py-4 mb-6 shadow-card">
        <div className="flex items-center gap-3">
          <span className="relative flex h-2.5 w-2.5">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-green-500"></span>
          </span>
          <div>
            <p className="text-sm font-semibold text-gray-800">Live threat monitoring active</p>
            <p className="text-xs text-gray-400 mt-0.5">
              Last scan {lastScanLabel} · {dashboard?.signalsProcessed ?? 0} signals processed
            </p>
          </div>
        </div>
        <button
          onClick={handleScan}
          disabled={isScanning}
          className="flex items-center gap-2 bg-brand-600 hover:bg-brand-700 disabled:bg-brand-300 text-white text-sm font-medium px-4 py-2 rounded-xl transition-colors"
        >
          {isScanning
            ? <><span className="animate-spin">⟳</span> Scanning...</>
            : <>🔍 Run AI Scan</>}
        </button>
      </div>

      <MetricCard label="CRITICAL RISKS"
        value={dashboard?.criticalRiskCount ?? 0}
        trend={dashboard?.criticalRiskTrend} color="red" />
      <MetricCard label="HIGH RISKS"
        value={dashboard?.highRiskCount ?? 0}
        trend={dashboard?.highRiskTrend} color="orange" />
      <MetricCard label="SKILLS GAPS"
        value={`${dashboard?.skillsGapPercentage ?? gapsData?.overallGapPercentage ?? 0}%`}
        trend={dashboard?.skillsGapTrend} color="blue" />
      <MetricCard label="ROADMAP PROGRESS"
        value={`${dashboard?.roadmapProgress ?? 0}%`}
        trend={dashboard?.roadmapTrend} color="green" />

      <div className="bg-white rounded-2xl shadow-card border border-surface-border p-6 mt-6">
        <div className="flex items-center justify-between mb-5">
          <div>
            <h2 className="text-base font-semibold text-gray-900">Recent Risk Alerts</h2>
            <p className="text-xs text-gray-400 mt-0.5">Open risks detected in the last scan</p>
          </div>
        </div>
        <RecentActivityFeed />
      </div>

      <div className="bg-white rounded-2xl shadow-card border border-surface-border p-6 mt-6">
        <div className="flex items-center justify-between mb-5">
          <div>
            <h2 className="text-base font-semibold text-gray-900">Skills Gap by Department</h2>
            <p className="text-xs text-gray-400 mt-0.5">% employees with identified gaps</p>
          </div>
        </div>
        {gapsLoading ? (
          <div className="animate-pulse space-y-3">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="h-6 rounded-xl bg-gray-100" />
            ))}
          </div>
        ) : (
          gapsData?.byDepartment?.map(dept => (
            <DepartmentGapRow key={dept.name} dept={dept} />
          ))
        )}
      </div>

      <ForesightInsightBox text={dashboard?.latestInsight} />
    </div>
  );
}
