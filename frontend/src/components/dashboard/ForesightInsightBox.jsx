export default function ForesightInsightBox({ text }) {
  return (
    <div className="mt-4 rounded-xl border border-brand-100 bg-brand-50 p-4">
      <div className="flex items-center gap-2 mb-2">
        <span className="text-brand-600 font-bold text-xs uppercase tracking-widest">
          ⚡ Foresight Insight
        </span>
      </div>
      <p className="text-sm text-brand-900/80 leading-relaxed">{text}</p>
    </div>
  );
}
